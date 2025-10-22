package Avianca.Steps;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.ibm.icu.text.SimpleDateFormat;

import net.thucydides.core.annotations.Step;
import org.openqa.selenium.OutputType;

public class Questions {

	private WebDriver driver;

	public Questions (WebDriver driver){
		this.driver = driver;
	}

	@Step("Verificar que el título de la página es correcto")
	public void tituloAssert() {

		String ActualTitle = driver.getTitle();
		Assert.assertEquals(ActualTitle, "Booking.com");
	}

	@Step("Tomar captura de pantalla")
	public void screenShot() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		File scr = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String filename = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		File dest = new File("C:\\Users\\jmedina\\Documents\\Captura" + filename + ".png");
		try {
			FileUtils.copyFile(scr, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	@Step("Verificar texto de contraseña")
	public void textoAssert() {

		Assert.assertEquals("Introduce tu contraseña", "Introduce tu contraseña");
	}

	@Step("Verificar texto de crear cuenta")
	public void textoCrearCuentaAssert() {

		Assert.assertEquals("Crea una contraseña", "Crea una contraseña");
	}
}