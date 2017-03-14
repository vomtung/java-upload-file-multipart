package example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class UploadImageMultipart
{
    
    public static void main(String[] args)
    {
        System.out.println("UploadFileAndroid");
        String url = "http://192.168.103.52:8080/iwfa-am-rest/v2/files";
        String token = "c77c2a503bfc10302927c1bca7bf5f3c0bfb9f687b088315dd7af15d0e0967129362ebde84216a8c";
        String username = "npthinh@tma.com.vn";
        //String srcFile = "/home/vmtung/workspace/example/src/example/aaa.jpg";
        //String srcFile = "/home/vmtung/Downloads/bbb.jpg";
        //String srcFile = "/home/vmtung/Documents/test_upload_abs.txt";
        String srcFile = "/home/vmtung/Pictures/aaa.jpg";
        String fileStyle = "Avatar";
        upload(url, token, username, srcFile, fileStyle);
        
    }
    
    private static void upload(String root, String token,
            String username, String sourceFileUri, String fileStyle)
    {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        String pathToOurFile = sourceFileUri;
        String urlServer = root;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        
        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File(
                    pathToOurFile));
            
            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();
            
            if (token != null)
            {
                connection.setRequestProperty("Authorization", "Basic " + token);
            }
            if (username != null)
            {
                connection.setRequestProperty("Username", username);
            }
            //if (!sourceFile.isFile())
            //{
            //    return reponseCode;
            //}
            if (fileStyle != null)
            {
                connection.setRequestProperty("file-type", fileStyle);
            }
            
            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            
            // Set HTTP method to POST.
            connection.setRequestMethod("POST");
            
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("User-Agent", "CodeJava Agent");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                            + pathToOurFile + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            
            outputStream.writeBytes(lineEnd);
            
            byte[]bytes = IOUtils.toByteArray(fileInputStream);
            outputStream.write(bytes);
            
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);
            
            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            
            // Read response from web server, which will trigger the
            // multipart HTTP request to be sent.
            BufferedReader httpResponseReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String lineRead;
            while ((lineRead = httpResponseReader.readLine()) != null)
            {
                System.out.println(lineRead);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
