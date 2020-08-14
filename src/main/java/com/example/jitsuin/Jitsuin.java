package com.example.jitsuin;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sun.identity.shared.debug.Debug;
import org.forgerock.json.JsonValue;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.forgerock.util.i18n.PreferredLocales;

import java.util.List;

@Node.Metadata(outcomeProvider = Jitsuin.MyOutcomeProvider.class, configClass = Jitsuin.Config.class)

public class Jitsuin implements Node {
    private final Config config;
    private final CoreWrapper coreWrapper;
    private final static String DEBUG_FILE = "Jitsuin";
    protected Debug debug = Debug.getInstance(DEBUG_FILE);
    JsonValue context_json;

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
        debug.error("+++     starting Jitsuin");

        context_json = context.sharedState.copy();
        String device_id = context_json.get("device_id").asString();

        ThingPosture thing_posture = new ThingPosture();
        String status = thing_posture.getStatus(config.Jitsuin_URL(), device_id, config.Compliance_String());
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
            return "";
        }

        @Attribute(order = 200)
        default String Client_Secret() {
            return "";
        }

        @Attribute(order = 300)
        default String Client_ID() {
            return "";
        }

        @Attribute(order = 400)
        default String Tenant() {
            return "";
        }

        @Attribute(order = 500)
        default String Compliance_String() {
            return "";
        }

    }

    @Inject
    public Jitsuin(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        this.coreWrapper = coreWrapper;
    }

}