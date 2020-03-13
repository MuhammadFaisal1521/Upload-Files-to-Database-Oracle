/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package SRL.oracle.apps.fnd.file.webui;

import SRL.oracle.apps.fnd.file.server.SrlFndAttachmentsViewImpl;
import SRL.oracle.apps.fnd.file.server.SrlFndAttachmentsViewRowImpl;
import SRL.oracle.apps.fnd.file.server.UploadFileAMImpl;

import fixedassetverification.oracle.apps.fa.iassets.server.EslFaAttachmentsViewImpl;
import fixedassetverification.oracle.apps.fa.iassets.server.EslFaAttachmentsViewRowImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.server.OADBTransaction;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.cabo.ui.data.DataObject;

import oracle.jbo.domain.BlobDomain;
import oracle.jbo.domain.Number;

/**
 * Controller for ...
 */
public class FileController extends OAControllerImpl
{
  public static final String RCS_ID="$Header$";
  public static final boolean RCS_ID_RECORDED =
        VersionInfo.recordClassVersion(RCS_ID, "%packagename%");

  /**
   * Layout and page setup logic for a region.
   * @param pageContext the current OA page context
   * @param webBean the web bean corresponding to the region
   */
  public void processRequest(OAPageContext pageContext, OAWebBean webBean)
  {
    super.processRequest(pageContext, webBean);
    
          UploadFileAMImpl am = (UploadFileAMImpl)pageContext.getApplicationModule(webBean);
          am.getSrlFndAttachmentsView1().executeQuery();
      
      }

  /**
   * Procedure to handle form submissions for form elements in
   * a region.
   * @param pageContext the current OA page context
   * @param webBean the web bean corresponding to the region
   */
  public void processFormRequest(OAPageContext pageContext, OAWebBean webBean)
  {
        ///Super object
    super.processFormRequest(pageContext, webBean);
    UploadFileAMImpl am = (UploadFileAMImpl)pageContext.getApplicationModule(webBean);
    
    
      //Button & Partial Process request  Event 
      if (pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM) != null) {
          String EventName = 
              new String(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM));
          // Coding for Event
          this.ActionEvent(pageContext, webBean, EventName,am);
      }
        
  }

    /// actionEvent handler
    private void ActionEvent(OAPageContext pageContext, OAWebBean webBean, String EventName,UploadFileAMImpl am) {
        
        if (EventName.equals("UploadFileToDB"))
            {
                this.UploadFileToDB(pageContext,webBean,am);
            }
    }

     ///Save Data
     public void Save(UploadFileAMImpl am) {
         am.getOADBTransaction().commit();
     }


    private void UploadFileToDB(OAPageContext pageContext, OAWebBean webBean ,UploadFileAMImpl am) 
      {  
        
              DataObject fileUploadData =(DataObject)pageContext.getNamedDataObject("AddAttachmentFile");
              String fileName = (String) fileUploadData.selectValue(null,"UPLOAD_FILE_NAME");
               if (fileName==null)
               {
                 throw new OAException("Please select attachment file for  upload.",OAException.ERROR);
               }
       
        SrlFndAttachmentsViewImpl AttachmentVo=(SrlFndAttachmentsViewImpl)am.getSrlFndAttachmentsView1();
      AttachmentVo.executeQuery();
      SrlFndAttachmentsViewRowImpl attachmentRow =(SrlFndAttachmentsViewRowImpl)AttachmentVo.createRow(); //Create new Record
      AttachmentVo.insertRow(attachmentRow);
      
      OADBTransaction DBT = (OADBTransaction)am.getDBTransaction();  /// DB Transaction 
      Number Attachment_id = new Number(DBT.getSequenceValue("SRL_FND_ATTACHMENT_SEQ")); //attachmentID
      //attachmentRow.setTransactionId(HeaderRow.getTransactionId()); //assign Transaction ID
      attachmentRow.setAttachmentsId(Attachment_id);
      attachmentRow.setFilename(fileName);
      attachmentRow.setContentType((String) fileUploadData.selectValue(null,"UPLOAD_FILE_MIME_TYPE"));
      attachmentRow.setFilesource(createBlobDomain(fileUploadData));
      
      this.Save(am);
      AttachmentVo.setMaxFetchSize(0);
      AttachmentVo.executeQuery();
    }
    
         private BlobDomain createBlobDomain(DataObject pfileUploadData)
         {
             // init the internal variables
                 InputStream in = null;
                 BlobDomain blobDomain = null;
                 OutputStream out = null;
                 
             try
             {
                 String pFileName =
                 (String) pfileUploadData.selectValue(null,"UPLOAD_FILE_NAME");
                 BlobDomain uploadedByteStream = (BlobDomain)pfileUploadData.selectValue(null,pFileName);
                 // Get the input stream representing the data from the client
                 in = uploadedByteStream.getInputStream();
                 // create the BlobDomain datatype to store the data in the db
                 blobDomain = new BlobDomain();
                 // get the outputStream for hte BlobDomain
                 out = blobDomain.getBinaryOutputStream();
                 byte buffer[] = new byte[8192];
                     for(int bytesRead = 0; (bytesRead = in.read(buffer, 0, 8192)) != -1;)
                         out.write(buffer, 0, bytesRead);
                     in.close();
             }
             catch (IOException e)
             {
                 e.printStackTrace();
             }
             catch (SQLException e)
             {
                 e.fillInStackTrace();
             }
             // return the filled BlobDomain
             return blobDomain;
         }

}
