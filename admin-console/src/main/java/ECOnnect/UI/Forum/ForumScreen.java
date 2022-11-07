package ECOnnect.UI.Forum;

import ECOnnect.UI.Interfaces.Screen;

public class ForumScreen extends Screen {

    public ForumScreen() {
        super(new ForumController());
    }

    public String getTitle() {
        return "Forum";
    }
}
