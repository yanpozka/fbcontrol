package exselenium;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.mail.MessagingException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author yanpozka
 */
public class ExSelenium {

    static String url = "https://facebook.com/login";//"http://127.0.0.1:8000/chupa/"; //  

    public static void main(String[] args) throws InterruptedException, IOException, MessagingException {
        if (args.length < 5) {
            System.exit(-1);
        }
        String useremail_fb = args[0];
        String passwd_fb = args[1];
        String urltocheck = args[2];
        String email_gmail = args[3];
        String pass_gmail = args[4];

        WebDriver browser = new PhantomJSDriver(DesiredCapabilities.chrome());

        browser.manage().window().setSize(new Dimension(1204, 860));
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        browser.get(url);
        String title = browser.getTitle();
        System.out.println("[+] Access to: " + title + ". Login with: " + useremail_fb);

        browser.findElement(By.id("email")).sendKeys(useremail_fb);
        browser.findElement(By.id("pass")).sendKeys(passwd_fb);

        WebElement checkbox_login = browser.findElement(By.id("persist_box"));
        if (checkbox_login.isSelected()) {
            checkbox_login.click();
        }

        browser.findElement(By.id("login_form")).submit();

        browser.get(urltocheck);
        System.out.println("[+] Page title: '" + browser.getTitle() + "'");

        WebElement elemcount;
        try {
            elemcount = (new WebDriverWait(browser, 10)).
                    until(ExpectedConditions.elementToBeClickable(
                                    By.cssSelector("#timeline_tab_content span._71u a.uiLinkSubtle")));
            String count = elemcount.getText();

            System.out.println("[+] Someone (maybe " + title + ") has [" + count + "] friends");
            Email mail = new Email(email_gmail, pass_gmail);
            if (args.length == 6) {
                mail.ccemail = args[5];
            }
            int result = mail.sendEmailOrNot(Integer.valueOf(count));

            if (result == 1) {
                System.out.println("[+] Email was sent with successful !");
            } else if (result == 0) {
                System.out.println("[+] There are not new friends so it isn't necessary sent email.");
            } else {
                System.out.println("[-] Impossible to send email :(");
            }

        } catch (NumberFormatException | IOException e) {
            System.out.println("[-] Something bad has occurred.");
            System.out.println(e.getMessage());
            //System.out.println(browser.getPageSource());

            File scrFile = ((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("screenshot_with_error.png"));
        } finally {
            browser.quit();
        }
    }
}
