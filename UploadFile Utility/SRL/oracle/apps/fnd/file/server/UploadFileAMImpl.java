package SRL.oracle.apps.fnd.file.server;

import oracle.apps.fnd.framework.server.OAApplicationModuleImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class UploadFileAMImpl extends OAApplicationModuleImpl {
    /**This is the default constructor (do not remove)
     */
    public UploadFileAMImpl() {
    }

    /**Sample main for debugging Business Components code using the tester.
     */
    public static void main(String[] args) {
        launchTester("SRL.oracle.apps.fnd.file.server", /* package name */
      "UploadFileAMLocal" /* Configuration Name */);
    }

    /**Container's getter for SrlFndAttachmentsView1
     */
    public SrlFndAttachmentsViewImpl getSrlFndAttachmentsView1() {
        return (SrlFndAttachmentsViewImpl)findViewObject("SrlFndAttachmentsView1");
    }
}
