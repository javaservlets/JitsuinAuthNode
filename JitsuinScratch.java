import com.example.jitsuin.ThingPosture;

// to run as a scratch file in IntelliJ
class JitsuinScratch {
    public static void main(String[] args) {
        String JITSUIN_URL = "https://demo-oper-avid.engineering-k8s-prod-2.dev.prod.jitsuin.io/archivist/v1/compliance/";
        String TOKEN_URL="https://login.microsoftonline.com/";
        String TENANT="f1f1a9e6-87a4-4276-afda-84836215a5f2";

        String CLIENT_ID = "d45d550a-d5b4-421b-8ece-f47ab44abaec";
        String CLIENT_SECRET = "pjxHB~g_r12n6.m8RqKWPUUbR_r6Tr_o0V";
        String CLIENT_RESOURCE = "https://demo-oper-avid.engineering-k8s-prod-2.dev.prod.jitsuin.io";

        String DEVICE_ID = "bb754212-f2fb-42ca-8cef-17c9090efe3";
        String COMPLIANCE_NAME = "compliant";

        ThingPosture thingInfo = new ThingPosture();
        thingInfo.generateToken(TOKEN_URL, TENANT, CLIENT_ID, CLIENT_SECRET, CLIENT_RESOURCE);
        thingInfo.getStatus(JITSUIN_URL, DEVICE_ID, COMPLIANCE_NAME);
    }
}
