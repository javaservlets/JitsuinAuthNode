import com.example.jitsuin.ThingPosture;

// to run as a scratch file in IntelliJ
class JitsuinScratch {
    public static void main(String[] args) {
        String JITSUIN_URL = "https://demo-oper-avid.engineering-k8s-prod-2.dev.prod.jitsuin.io/archivist/v1/compliance/assets/";
        String TOKEN_URL="https://login.microsoftonline.com/";
        String TENANT="f1f1a9e6-87a4-4276-afda-84836215a5f2";

        String CLIENT_ID = "d45d550a-d5b4-421b-8ece-f47ab44abaec";
        String CLIENT_SECRET = "pjxHB~g_r12n6.m8RqKWPUUbR_r6Tr_o0V";
        String CLIENT_RESOURCE = "https://demo-oper-avid.engineering-k8s-prod-2.dev.prod.jitsuin.io/.default";

        String DEVICE_ID = "f970e497-0574-491d-9549-67b15ec6b171";
        String COMPLIANCE_NAME = "compliance";

        ThingPosture thingInfo = new ThingPosture();
        thingInfo.generateToken(TOKEN_URL, TENANT, CLIENT_ID, CLIENT_SECRET, CLIENT_RESOURCE);
        thingInfo.getStatus(JITSUIN_URL, DEVICE_ID, COMPLIANCE_NAME);
    }
}
