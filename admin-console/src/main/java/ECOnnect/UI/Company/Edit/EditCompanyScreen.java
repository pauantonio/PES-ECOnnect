package ECOnnect.UI.Company.Edit;

import ECOnnect.UI.Interfaces.Screen;

public class EditCompanyScreen extends Screen {
    public EditCompanyScreen(){
        super(new EditCompanyController());
    }
    public String getTitle(){return "Edit company";}
}
