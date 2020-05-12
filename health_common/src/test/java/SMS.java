import com.aliyuncs.exceptions.ClientException;
import com.itheima.Utils.SMSUtils;
import org.junit.Test;

public class SMS {
@Test
    public void sms01() throws ClientException {
        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,"15138039605","pezzzisbiubiubiu");


}
}
