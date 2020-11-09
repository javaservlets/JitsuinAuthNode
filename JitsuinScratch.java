import com.example.jitsuin.ThingPosture;

class JitsuinScratch {
    public static void main(String[] args) {
        String JITSUIN_URL = "https://playground-avid.engineering-k8s-poc-1.dev.poc.jitsuin.io/archivist/v1/compliance/assets/";
        String TOKEN_URL="https://login.microsoftonline.com/";
        String CLIENT_SECRET = ".uQ9wBT.ftn3X~qRR-Hr7eFic2OMfo2JX3";
        String CLIENT_ID = "54c65c80-2734-47d7-919e-3d679b6a4905";
        String CLIENT_RESOURCE = "https://playground-avid.engineering-k8s-poc-1.dev.poc.jitsuin.io/.default";
        String TENANT="e6cd7cbd-4331-4942-b28d-a327d99a088a";
        String COMPLIANCE_NAME = "\\\"compliant\\\":true";

        String DEVICE_ID = "0e589f7c-c480-4302-925a-9b243a553ade";

        ThingPosture thingInfo = new ThingPosture();
        thingInfo.generateToken(TOKEN_URL, TENANT, CLIENT_ID, CLIENT_SECRET, CLIENT_RESOURCE);
        thingInfo.getStatus(JITSUIN_URL, DEVICE_ID, COMPLIANCE_NAME);
    }
}
