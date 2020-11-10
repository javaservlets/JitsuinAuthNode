import com.example.jitsuin.ThingPosture;

class JitsuinScratch {
    public static void main(String[] args) {
        String JITSUIN_URL = "https://playground-avid.engineering-k8s-poc-1.dev.poc.jitsuin.io/archivist/v1/compliance/assets/";
        String TOKEN_URL="https://login.microsoftonline.com/";
        String CLIENT_SECRET = "your value here";
        String CLIENT_ID = "your value here";
        String CLIENT_RESOURCE = "https://playground-avid.engineering-k8s-poc-1.dev.poc.jitsuin.io/.default";
        String TENANT="your value here";
        String COMPLIANCE_NAME = "\\\"compliant\\\":true";

        String DEVICE_ID = "0e589f7c-c480-4302-925a-9b243a553ade";

        ThingPosture thingInfo = new ThingPosture();
        thingInfo.generateToken(TOKEN_URL, TENANT, CLIENT_ID, CLIENT_SECRET, CLIENT_RESOURCE);
        thingInfo.getStatus(JITSUIN_URL, DEVICE_ID, COMPLIANCE_NAME);
    }
}
