package dev.Program.Backend.BusinessLayer.Extractions;

import java.util.List;

import dev.Program.Backend.BusinessLayer.Process.Process;
import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Server.ChromeWindowToSendSocket;
import dev.Program.Backend.BusinessLayer.Server.ExtensionSocketServer;
import dev.Program.DTOs.ChromeWindow;

public class ChromeExtracion implements Extraction<ChromeWindow>{

    private ExtensionSocketServer server;
    public  ProcessController pc;

    public ChromeExtracion(ExtensionSocketServer server,ProcessController pc){
        this.pc = pc;
        this.server = server;
    }

    @Override
    public ChromeWindow extract(String id) {
        return null;
    }

    @Override
    public ChromeWindow close(ChromeWindow window) {
        insureConnection();
        return server.closeAllTabs(window);
    }

    @Override
    public ChromeWindow execute(ChromeWindow window) {
        insureConnection();
        ChromeWindowToSendSocket recieved = server.openWindows(window);
        return new ChromeWindow(recieved, true);
    }

    @Override
    public List<ChromeWindow> getAllInstances() {
        insureConnection();
        return server.getCurrentGoogleOpenedWindows();
    }

    private void insureConnection() {
        // TODO if there is no connection To connect
    }

     
    
}
