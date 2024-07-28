package com.leandroperez.taf.runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SelectClasspathResource("features/mobile")
@SelectClasspathResource("com/koushick/BDD/Steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@IncludeTags({"tag1", "tag2"})
public class RunMobileTest {
}
