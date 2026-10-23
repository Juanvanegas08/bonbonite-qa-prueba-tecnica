package co.juanvanegas.qa;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class AcceptanceTestSuite {}
