package my.project.proveofconcept;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;
import org.junit.runner.RunWith;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import dalvik.system.DexFile;

public class Runner extends AndroidJUnitRunner {

    private final String LOG_TAG = "RUNNER";
    private Process p;
    private File logFile;

    @Override
    public void onCreate(Bundle arguments) {

        Log.d("--->>>>>. iN Runner","onCreate");
        logFile = new File( new File(arguments.getString("testfile")).getParentFile(), "app.log");

        try {
            Runtime.getRuntime().exec("logcat -c"); // clear logcat buffer
            logFile.delete();
        }catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("TESTPATH:"+arguments.getString("testfile"));
            DexFile dexFile = DexFile.loadDex(arguments.getString("testfile"), new File(getContext().getCacheDir(), "cachedTest.odex").getAbsolutePath(),0);
            ClassLoader classLoader = getContext().getClassLoader();
            Enumeration<String> classes = dexFile.entries();
            ArrayList<Class> classList = new ArrayList<Class>();
            while (classes.hasMoreElements()){
                String s = classes.nextElement();
                Log.d("Classes:" , s);
                Class c = dexFile.loadClass(s,classLoader);
                if(null != c.getAnnotation(RunWith.class) )  classList.add(c);

            }
            arguments.putString("class", generateClassString(classList));

        } catch (IOException e) {
            Log.d(LOG_TAG, "Opening Dex failed!");
            e.printStackTrace();
        }

        arguments.putString("listener", "my.project.proveofconcept.CustomRunListener");

        this.setAutomaticPerformanceSnapshots();

        super.onCreate(arguments);

    }

    @Override
    public void finish(int resultCode, Bundle results) {

        for (String key : results.keySet()) {
            Object value = results.get(key);
            Log.d(LOG_TAG, String.format("%s : %s",key, value.toString()));
        }

        try {

            logFile.createNewFile();

            String cmd = "logcat -d -f " + logFile.getAbsolutePath() + " TestRunner:V LISTENER:D RUNNER:D *:S" ; // redirect logcat output to file and write log in it
            System.out.println("LOGGER FILE :"+ cmd);
            p = Runtime.getRuntime().exec(cmd);

        } catch (IOException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        Log.d(LOG_TAG, "finish");
        super.finish(resultCode, results);


    }

    private String generateClassString(ArrayList<Class> classes) {
        String r = classes.get(0).getName();
        for (int i = 1; i< classes.size(); i++) {
            r += "," + classes.get(i).getName();
        }
        Log.d("CLASSES R:",r);

        return r;
    }
}
