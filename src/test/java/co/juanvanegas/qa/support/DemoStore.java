package co.juanvanegas.qa.support;

import co.juanvanegas.qa.model.TestCustomer;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/** Tienda mínima para verificar el código sin escribir datos en Bon-Bonite.
 * No replica las reglas internas, integraciones, impuestos ni pasarela del sitio real. */
public final class DemoStore {
    private static HttpServer server;
    private static final Map<String,Customer> customers=new ConcurrentHashMap<>();
    private static final Map<String,Session> sessions=new ConcurrentHashMap<>();
    private static final AtomicLong orders=new AtomicLong(1000);
    private static final long PRICE=327900;
    private static final class Customer {
        final String id,last,email,password; String first;
        Customer(String id,String first,String last,String email,String password) {
            this.id=id;this.first=first;this.last=last;this.email=email;this.password=password;
        }
    }
    private static final class Session { String customer; int quantity; String size; long order; }
    public static synchronized void start() throws IOException {
        if(server!=null) return;
        Settings.requireDemo(); customers.clear(); sessions.clear();
        TestCustomer c=TestCustomer.existing();
        customers.put(c.id(),new Customer(c.id(),c.first(),c.last(),c.email(),c.password()));
        server=HttpServer.create(new InetSocketAddress("127.0.0.1",URI.create(Settings.base()).getPort()),0);
        server.createContext("/",DemoStore::handle);
        server.setExecutor(Executors.newCachedThreadPool(r->{Thread t=new Thread(r);t.setDaemon(true);return t;}));
        server.start();
    }
    public static synchronized void stop() { if(server!=null){server.stop(0);server=null;} }
    private static String esc(String v) { return v.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;"); }
    private static Map<String,String> fields(HttpExchange x) throws IOException {
        Map<String,String> data=new HashMap<>();
        String body=new String(x.getRequestBody().readAllBytes(),StandardCharsets.UTF_8);
        for(String p:body.split("&")){String[] a=p.split("=",2); if(a.length==2)data.put(URLDecoder.decode(a[0],StandardCharsets.UTF_8),URLDecoder.decode(a[1],StandardCharsets.UTF_8));}
        return data;
    }
    private static Session session(HttpExchange x) {
        String cookie=x.getRequestHeaders().getFirst("Cookie"); String id=null;
        if(cookie!=null)for(String c:cookie.split(";"))if(c.trim().startsWith("qa_session="))id=c.trim().substring(11);
        if(id==null || !sessions.containsKey(id)){
            id=UUID.randomUUID().toString(); sessions.put(id,new Session());
            x.getResponseHeaders().add("Set-Cookie","qa_session="+id+"; Path=/; HttpOnly; SameSite=Lax");
        }
        return sessions.get(id);
    }
    private static void redirect(HttpExchange x,String path) throws IOException {
        x.getResponseHeaders().add("Location",path); x.sendResponseHeaders(303,-1);x.close();
    }
    private static void page(HttpExchange x,int status,String body) throws IOException {
        String html="<!doctype html><html lang='es'><meta charset='utf-8'><title>Tienda QA DEMO</title><style>body{font:17px Arial;max-width:920px;margin:40px auto;color:#17253c}header{padding:16px;background:#fff1cd}label{display:block;margin:10px 0}input,select,button{font:inherit;padding:8px}button,a{margin:8px}form{padding:18px;border:1px solid #ccd2dc}table{border-collapse:collapse}td,th{padding:14px;border-bottom:1px solid #ddd}</style><header>DEMO LOCAL. Datos sintéticos y pago ficticio. No es el sitio Bon-Bonite.</header><main>"+body+"</main></html>";
        byte[] bytes=html.getBytes(StandardCharsets.UTF_8);x.getResponseHeaders().set("Content-Type","text/html; charset=utf-8");
        x.getResponseHeaders().set("Cache-Control","no-store"); x.sendResponseHeaders(status,bytes.length);x.getResponseBody().write(bytes);x.close();
    }
    private static String input(String id,String name,String type,String label,String value) {
        return "<label>"+label+" <input required id='"+id+"' name='"+name+"' type='"+type+"' value='"+esc(value)+"'></label>";
    }
    private static void handle(HttpExchange x) throws IOException {
        try { route(x); } catch(Exception e) { page(x,500,"<h1>Error DEMO</h1><p>"+esc(e.getClass().getSimpleName())+"</p>"); }
    }
    private static void route(HttpExchange x) throws IOException {
        String path=x.getRequestURI().getPath(); Session s=session(x);
        Map<String,String> f="POST".equals(x.getRequestMethod())?fields(x):Map.of();
        if(path.equals("/logout/")){s.customer=null;s.quantity=0;redirect(x,"/mi-cuenta/");return;}
        if(path.equals("/register/")){
            for(String k:List.of("username","first_name","last_name","email","password","password2","privacy_policy_reg"))
                if(f.getOrDefault(k,"").isBlank()){page(x,400,"Falta "+k);return;}
            if(!f.get("password").equals(f.get("password2")) || customers.containsKey(f.get("username"))){page(x,400,"Cuenta duplicada o contraseñas diferentes");return;}
            Customer c=new Customer(f.get("username"),f.get("first_name"),f.get("last_name"),f.get("email"),f.get("password"));
            customers.put(c.id,c);s.customer=c.id;redirect(x,"/mi-cuenta/");return;
        }
        if(path.equals("/login/")){
            Customer c=customers.get(f.getOrDefault("username",""));
            if(c==null||!c.password.equals(f.get("password"))){page(x,401,"Credenciales incorrectas");return;}
            s.customer=c.id;redirect(x,"/mi-cuenta/");return;
        }
        if(path.equals("/mi-cuenta/") && s.customer==null){
            page(x,200,"<h1>Mi cuenta DEMO</h1><div id='customer_login'><form method='post' action='/login/'>"+
                input("username","username","text","Cédula","")+input("password","password","password","Contraseña","")+
                "<button name='login'>Iniciar sesión</button></form><button id='show_register' onclick=\"document.getElementById('customer_register').hidden=false\">Regístrate</button></div>"+
                "<section id='customer_register' hidden><h2>Crea tu cuenta</h2><form id='form-register' action='/register/' method='post'>"+
                input("reg_username","username","text","Cédula DEMO","")+input("first_name","first_name","text","Nombres","")+
                input("last_name","last_name","text","Apellidos","")+input("reg_email","email","email","Correo","")+
                input("reg_password","password","password","Contraseña","")+input("reg_password2","password2","password","Confirmar contraseña","")+
                "<label><input id='privacy_policy_reg' name='privacy_policy_reg' type='checkbox' required>Acepto datos ficticios DEMO</label><button name='register'>Registrarme</button></form></section>");return;
        }
        if(s.customer==null){redirect(x,"/mi-cuenta/");return;}
        Customer c=customers.get(s.customer);
        String logout="<a id='logout' href='/logout/'>Cerrar sesión</a>";
        if(path.equals("/mi-cuenta/")){
            page(x,200,"<h1 id='account-home'>Mi cuenta DEMO</h1><p id='account-name'>"+esc(c.first)+"</p><p id='account-id'>"+esc(c.id)+"</p><p id='account-email'>"+esc(c.email)+"</p><a href='/mi-cuenta/edit-account/'>Editar perfil</a>"+logout);return;
        }
        if(path.equals("/mi-cuenta/edit-account/")){
            boolean saved=!f.isEmpty(); if(saved && !f.getOrDefault("account_first_name","").isBlank())c.first=f.get("account_first_name");
            page(x,200,(saved?"<p class='woocommerce-message'>Datos guardados</p>":"")+"<h1>Editar datos DEMO</h1><form method='post'>"+
                input("account_first_name","account_first_name","text","Nombres",c.first)+"<button name='save_account_details'>Guardar cambios</button></form>"+logout);return;
        }
        if(path.equals("/producto/zueco-demo/")){
            page(x,200,"<h1 class='product_title'>Zueco DEMO</h1><p class='price'>$327,900 COP</p><form action='/add/' method='post'><label>Talla <select id='pa_talla' name='attribute_pa_talla' required><option value=''>Selecciona</option><option value='37'>37</option><option value='38'>38</option></select></label>"+
                input("quantity","quantity","number","Cantidad","1")+"<button class='single_add_to_cart_button'>Añadir al carrito</button></form>");return;
        }
        if(path.equals("/add/")){
            s.quantity=Integer.parseInt(f.getOrDefault("quantity","0"));s.size=f.get("attribute_pa_talla");
            if(s.quantity<1||s.quantity>5||!List.of("37","38").contains(s.size)){page(x,400,"Sin disponibilidad DEMO");return;}
            redirect(x,"/carrito/");return;
        }
        if(path.equals("/carrito/")){
            page(x,200,"<h1>Carrito DEMO</h1><table><tr><th>Producto</th><th>Talla</th><th>Cantidad</th><th>Precio</th><th>Subtotal COP</th></tr><tr class='cart_item'><td class='product-name'>Zueco DEMO</td><td class='variation-Talla'>"+esc(s.size)+"</td><td><input class='qty' value='"+s.quantity+"' readonly></td><td class='product-price' data-amount='"+PRICE+"'>"+PRICE+"</td><td class='product-subtotal' data-amount='"+(PRICE*s.quantity)+"'>"+(PRICE*s.quantity)+"</td></tr></table><a id='checkout' href='/finalizar-compra/'>Finalizar compra DEMO</a>");return;
        }
        if(path.equals("/finalizar-compra/")){
            if(s.quantity<1){page(x,400,"Carrito vacío");return;}
            page(x,200,"<h1>Checkout DEMO</h1><form method='post' action='/place-order/'>"+
                input("billing_first_name","billing_first_name","text","Nombres","")+input("billing_last_name","billing_last_name","text","Apellidos","")+
                input("billing_address_1","billing_address_1","text","Dirección","")+input("billing_city","billing_city","text","Ciudad","")+
                "<label><input required type='radio' id='payment_method_demo' name='payment_method' value='demo'>Pago ficticio DEMO</label><label><input required type='checkbox' id='terms' name='terms'>Acepto términos DEMO</label><button id='place_order'>Confirmar pedido ficticio</button></form>");return;
        }
        if(path.equals("/place-order/")){
            if(s.quantity<1||!"demo".equals(f.get("payment_method"))||!f.containsKey("terms")){page(x,400,"Pedido inválido");return;}
            for(String k:List.of("billing_first_name","billing_last_name","billing_address_1","billing_city"))if(f.getOrDefault(k,"").isBlank()){page(x,400,"Falta dato de facturación");return;}
            s.order=orders.incrementAndGet();redirect(x,"/order-received/"+s.order+"/");return;
        }
        if(path.startsWith("/order-received/")&&s.order>0){
            page(x,200,"<h1 class='woocommerce-thankyou-order-received'>Pedido DEMO recibido</h1><p id='order-number'>DEMO-"+s.order+"</p><p id='order-total' data-amount='"+(PRICE*s.quantity)+"'>"+(PRICE*s.quantity)+" COP</p><p id='payment-status'>Pago ficticio aprobado</p>");return;
        }
        page(x,404,"Página DEMO no encontrada");
    }
}
