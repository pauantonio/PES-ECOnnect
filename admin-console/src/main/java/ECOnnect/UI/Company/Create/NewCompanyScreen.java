package ECOnnect.UI.Company.Create;

import ECOnnect.UI.Interfaces.Screen;

public class NewCompanyScreen extends Screen {
    public NewCompanyScreen(){
        super(new NewCompanyController());
    }
    public String getTitle(){return "New Company Form";}
}
