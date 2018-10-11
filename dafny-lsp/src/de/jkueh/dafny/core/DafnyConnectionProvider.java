package de.jkueh.dafny.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.statushandlers.StatusManager;




/**
 * Stellt Komponenten bereit um mit dem DafnyServer zu kommunizieren
 * @author Jannik Kühnemundt
 *
 */
public class DafnyConnectionProvider {

	
	public static final String DAFNY_SERVER_EOM = "[[DAFNY-SERVER: EOM]]";
	private ProcessBuilder builder;
	private Process dafnyProcess;
	private InputStream inputstream;
	private OutputStream outputstream;
	private InputStreamReader inputStreamReader;
	private PrintWriter printWriter;
	
	/**
	 * Konstruktor der Klasse DafnyConnectionProvider, stellt eine Verbindung zu Dafny her
	 */
	public DafnyConnectionProvider() {

		// Einstellungsdaten holen
		IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, "de.jkueh.dafnylcplugin");
		
		// Betriebssystem abfragen, macOS und Linux benötigen mono
		if(System.getProperty("os.name").startsWith("Mac") || System.getProperty("os.name").startsWith("Linux")) {
			builder = new ProcessBuilder(prefs.getString("MONOPATH") + "/mono",prefs.getString("DAFNYPATH") + "/DafnyServer.exe");
		}
		else {
			builder = new ProcessBuilder(prefs.getString("DAFNYPATH") + "/DafnyServer.exe");

		}
		builder.redirectErrorStream(true);	 
		
		try {
				dafnyProcess = builder.start();
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, "dafnylsplugin", IStatus.OK, "Can't find DafnyServer or mono. Please set path in settings", null);
			  StatusManager.getManager().handle(status, StatusManager.SHOW);
	    } 
		
		// Streams vom Prozess holen
		inputstream = dafnyProcess.getInputStream();
		outputstream = dafnyProcess.getOutputStream();
		printWriter = new PrintWriter(outputstream);
		try {
			inputStreamReader = new InputStreamReader(inputstream, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sendet Daten an den Dafny-Prozess
	 * @param sourcecode Quellcode
	 * @return Liste mit Diagnostic Objekten
	 */
	public List<Diagnostic> sendData(String sourcecode, String filename){
		
		DafnyMessage query = new DafnyMessage(sourcecode, false, filename);
		printWriter.println(query.encode());
		printWriter.flush();
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String line = null;
		StringBuilder response = new StringBuilder();
	    try {

	    	while((line = reader.readLine()) != null) {
	    	    response.append(line);
	    	    response.append("\n");
	    	    if (line.contains(DAFNY_SERVER_EOM)) {
	    	    	break;
	    	    }
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	    // Parser Objekt erstellen
    	DafnyParser parser = new DafnyParser();
        return parser.parseServerResponse(response.toString());
    }

	/**
	 * Schließt den Stream und beendet den Prozess
	 */
	public void disconnect() {
		try {
			outputstream.close();
			dafnyProcess.destroy();

		} catch (IOException e) {
			e.printStackTrace();
		}
		dafnyProcess.destroyForcibly();
	}
}

				