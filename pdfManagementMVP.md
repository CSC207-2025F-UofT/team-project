PDF Management

4.1: UploadPdf:
Precondition: User is logged In
The user clicks the “Upload PDF” button.
The system opens a file picker.
The user selects a PDF file from their computer.
The system checks that the file is a PDF and within the size limit.
The system saves the file to the researcher’s account.
The system shows a success message and adds the PDF to the researcher’s list of uploaded PDFs.

4.2 AssociatePdfToPaper
Precondition: User is Logged In and at least one uploaded PDF
The user opens a paper in their library.
The user clicks the “Attach PDF” button.
The system shows the researcher’s list of uploaded PDFs.
The user selects a PDF from the list.
The system links the PDF to the paper.
The system shows a success message and a PDF icon on the paper.

4.3 OpenPDF
Precondition: User is Logged In and at least one uploaded PDF
The user opens a paper in their library, or their list of uploaded PDFs.
The user clicks “Open PDF”.
The system checks that the PDF belongs to the User.
The system opens the PDF.

4.4 DeletePDF
Precondition: User is Logged In and at least one uploaded PDF
The User opens their list of uploaded PDFs, or a paper with an attached PDF.
The User clicks “Delete PDF”.
The system asks for confirmation and names the file and any linked paper.
The User presses the Confirm button.
The system deletes the file and removes the link from the paper.
The system shows a success message. The paper stays in the library without a PDF.
