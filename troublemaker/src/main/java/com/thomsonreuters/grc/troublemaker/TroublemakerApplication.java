package com.thomsonreuters.grc.troublemaker;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.thomsonreuters.grc.tools.cmdb.model.ServerProfileTargetAppNode;
import com.thomsonreuters.grc.tools.cmdb.model.Target;
import com.thomsonreuters.grc.troublemaker.model.Application;
import com.thomsonreuters.grc.troublemaker.model.ApplicationNode;
import com.thomsonreuters.grc.troublemaker.model.Namespace;
import com.thomsonreuters.grc.wco.environment.WcoEnvironments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EnableAutoConfiguration
@RestController
@SpringBootApplication
public class TroublemakerApplication {

    public static final String START_SERVER = "START";
    public static final String STOP_SERVER = "STOP";
    public static final String RESTART_SERVER = "RESTART";

    public static final String THREAD_DUMP = "THREADDUMP";
    public static final String HEAD_DUMP = "HEADDUMP";

    public TroublemakerApplication() {
        //WcoEnvironments.ENV_MAP.values().stream().forEach(e -> e.getCmdb().getTargets()); // warm up
    }

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/namespaces")
    List<Namespace> getNamespaces() {
        return WcoEnvironments.ENV_MAP.values().stream().map(e -> new Namespace(e.getCmdbNamespace())).collect(Collectors.toList());
    }

    @RequestMapping("/namespace/{namespaceName}/applications")
    List<Application> getApplications(@PathVariable("namespaceName") String namespaceName) {
        return ((Collection<Target>) WcoEnvironments.ENV_MAP.get(new Namespace(namespaceName).getEnvironment()).getCmdb().getTargets()).stream().map(Application::new).collect(Collectors.toList());
    }

    @RequestMapping("/namespace/{namespaceName}/application/{application}/servers")
    List<ApplicationNode> getServers(@PathVariable("namespaceName") String namespaceName, @PathVariable("application") String application) {

        List<ApplicationNode> nodes = ((List<ServerProfileTargetAppNode>) ((Target) WcoEnvironments.ENV_MAP.get(new Namespace(namespaceName).getEnvironment()).getCmdb().getTarget(application)).getNodes()).stream().map(ApplicationNode::new).collect(Collectors.toList());

        return nodes;
    }

    @RequestMapping("/namespace/{namespaceEnv}/application/{application}/server/{server}/{action}")
    String startStopServer(@PathVariable("namespaceEnv") String namespaceEnv, @PathVariable("application") String application,
                        @PathVariable("server") String server, @PathVariable("action") String action) throws JSchException, IOException {

        Session session = startSession(server);

        boolean isRunning = getApplicationStatus(application, session);

        String output = null;


        switch (action.toUpperCase()) {
            case START_SERVER:

                if (isRunning) {
                    output = "Application is running already.";

                } else {

                    output = serverCommand(session, application, "./startServer.sh");
                }
                break;
            case STOP_SERVER:
                if (!isRunning) {
                    output = "Application is stopped already.";

                } else {
                    output = serverCommand(session, application, "./stopServer.sh");

                }
                break;
            case RESTART_SERVER:

                if (isRunning) {
                    output = serverCommand(session, application, "./stopServer.sh");
                }
                output = output + serverCommand(session, application, "./startServer.sh");
                break;
            default:
                output = "Action is not supported";

        }

        session.disconnect();
        return output;
    }

    private Session startSession(String server) throws JSchException {
        JSch jsch = new JSch();

        String user = "asadmin";
        String password = "east";

        Session session = jsch.getSession(user, server, 22);

        session.setPassword(password);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        return session;
    }


    private String readCommandOutput(ChannelShell ce) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));

        String line;

        String output = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            output += line;
        }

        return output;
    }

    private boolean getApplicationStatus(String appServer, Session session) throws IOException, JSchException {

        String output = serverCommand(session, appServer, "ps -ef | grep " + appServer);

        return output.contains("QA1");
    }

    private String serverCommand(Session session, String application,
                                 String command) throws JSchException, IOException {
        ChannelShell ce = (ChannelShell) session.openChannel("shell");

        String filename = "shellscript.sh";
        File fstream = new File(filename);

        try {
            // Create file
            PrintStream out = new PrintStream(new FileOutputStream(fstream));

            out.println("cd /appserver/tomcat/" + application + "/bin");

            out.println(command);
            out.println("exit\n");

            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        FileInputStream fin = new FileInputStream(fstream);
        byte fileContent[] = new byte[(int) fstream.length()];
        fin.read(fileContent);
        InputStream in = new ByteArrayInputStream(fileContent);

        ce.setInputStream(in);

        ce.connect();

        String output = readCommandOutput(ce);
        ce.disconnect();

        return output;
    }

    @RequestMapping("/namespace/{namespaceEnv}/application/{application}/server/{dump}")
    String threadDump(@PathVariable("namespaceEnv") String namespaceEnv, @PathVariable("application") String application,
                      @PathVariable("server") String server, @PathVariable("dump") String dump) {

        switch (dump.toUpperCase()) {
            case THREAD_DUMP:
                return "OK";
            case HEAD_DUMP:
                return "FAILED";
            default:
                return "Dump is not supported";

        }
    }


    @RequestMapping("/namespace/{namespaceEnv}/application/{application}/server/{server}/log")
    String getLog(@PathVariable("namespaceEnv") String namespaceEnv, @PathVariable("application") String application,
                  @PathVariable("server") String server) throws JSchException, IOException {

        Session session = startSession(server);

        String output = serverCommand(session, application,
                "cat /log/" + application + "/" + application + ".log");

        session.disconnect();

        return output;

    }

    public static void main(String[] args) {
        SpringApplication.run(TroublemakerApplication.class, args);
    }
}
