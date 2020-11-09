package com.example.jitsuin;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.forgerock.json.JsonValue;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.forgerock.util.i18n.PreferredLocales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Node.Metadata(outcomeProvider =
        Jitsuin.MyOutcomeProvider.class,
        configClass = Jitsuin.Config.class,
        tags = {"iot"}
        )

public class Jitsuin implements Node {
    private final Config config;
    private final CoreWrapper coreWrapper;
    private final static String DEBUG_FILE = "Jitsuin";
    private final Logger debug = LoggerFactory.getLogger(Jitsuin.class);
    //protected Debug debug = Debug.getInstance(DEBUG_FILE);
    JsonValue context_json;

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
        debug.error("+++     starting Jitsuin");

        context_json = context.sharedState.copy();
        String device_id = context_json.get("username").asString();

        ThingPosture thingInfo = new ThingPosture();
        thingInfo.generateToken(config.Jitsuin_Token_URL(), config.Tenant(), config.Client_ID(), config.Client_Secret(), config.Client_Resource());
        String status = thingInfo.getStatus(config.Jitsuin_URL(), device_id, config.Compliance_String());
        Action action = null ;

        debug.error("+++   action.process  " + status.toString());

        if (status.equals("compliant")) {
            action = goTo(MyOutcome.COMPLIANT).build();
        } else if (status.equals("noncompliant")) {
            action = goTo(MyOutcome.NONCOMPLIANT).build();
        } else if (status.equals("unknown")) {
            action = goTo(MyOutcome.UNKNOWN).build();
        } else {
            action = goTo(MyOutcome.CONNECTION_ERROR).build();
        }
        return action;
    }


    public enum MyOutcome {
        /**
         * Successful parsing of cert for a dev id.
         */
        COMPLIANT,
        /**
         * dev id found in cert but device isn't compliant
         */
        NONCOMPLIANT,
        /**
         * no device found with ID from cert
         */
        UNKNOWN,
        /**
         * no connection to mdm
         */
        CONNECTION_ERROR,
    }

    private Action.ActionBuilder goTo(MyOutcome outcome) {
        return Action.goTo(outcome.name());
    }

    public static class MyOutcomeProvider implements org.forgerock.openam.auth.node.api.OutcomeProvider {
        @Override
        public List<Outcome> getOutcomes(PreferredLocales locales, JsonValue nodeAttributes) {
            return ImmutableList.of(
                    new Outcome(MyOutcome.COMPLIANT.name(), "Compliant"),
                    new Outcome(MyOutcome.NONCOMPLIANT.name(), "Non Compliant"),
                    new Outcome(MyOutcome.UNKNOWN.name(), "Unknown"),
                    new Outcome(MyOutcome.CONNECTION_ERROR.name(), "Connection Error"));
        }
    }

    public interface Config {

        @Attribute(order = 100)
        default String Jitsuin_URL() {
            return "https://demo-oper-avid.engineering-k8s-prod-2.dev.prod.jitsuin.io/archivist/v1/compliance";
        }

        @Attribute(order = 101)
        default String Jitsuin_Token_URL() {
            return "https://login.microsoftonline.com";
        }

        @Attribute(order = 200)
        default String Client_Secret() {
            return "pjxHB~g_r12n6.m8RqKWPUUbR_r6Tr_o0V";
        }

        @Attribute(order = 300)
        default String Client_ID() {
            return "d45d550a-d5b4-421b-8ece-f47ab44abaec";
        }

        @Attribute(order = 400)
        default String Client_Resource() {
            return "https://demo-oper-avid.engineering-k8s-prod-2.dev.prod.jitsuin.io";
        }

        @Attribute(order = 500)
        default String Tenant() {
            return "f1f1a9e6-87a4-4276-afda-84836215a5f2";
        }

        @Attribute(order = 600)
        default String Compliance_String() {
            return "compliant";
        }

    }

    @Inject
    public Jitsuin(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        this.coreWrapper = coreWrapper;
    }

}