package ECOnnect.UI.Company.Questions;

import ECOnnect.UI.Interfaces.Screen;

public class CompanyQuestionsScreen extends Screen {
        
    public CompanyQuestionsScreen() {
        super(new CompanyQuestionsController());
    }
    
    public String getTitle() {
        return "Questions for all companies";
    }
}
