package ECOnnect.UI.Company;

import ECOnnect.UI.Interfaces.Screen;

public class CompanyScreen extends Screen {

    public CompanyScreen() {
        super(new CompanyController());
    }

    public String getTitle() {
        return "Companies";
    }
}
