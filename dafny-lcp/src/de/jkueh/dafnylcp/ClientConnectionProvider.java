package de.jkueh.dafnylcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.eclipse.lsp4e.server.StreamConnectionProvider;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;


import de.jkueh.dafny.server.DafnyLanguageServer;

/**
 * Klasse repräsentiert den Client, der sich mit dem Language Server verbindet
 * @author Jannik Kühnemundt
 *
 */
public class ClientConnectionProvider implements StreamConnectionProvider{

	private InputStream inputStream ;
	private OutputStream outputStream;

	/**
	 * Startet den Client und Langauge Server und verbindet beide
	 */
	@Override
	public void start() throws IOException {
		DafnyLanguageServer dafnyLanguageServer = new DafnyLanguageServer();
		PipedInputStream in = new PipedInputStream();
		PipedOutputStream out = new PipedOutputStream();
		PipedInputStream in2 = new PipedInputStream();
		PipedOutputStream out2 = new PipedOutputStream();
		
		in.connect(out2);
		out.connect(in2);
		
		inputStream = in;
		outputStream = out;
		Launcher<LanguageClient> client = LSPLauncher.createServerLauncher(dafnyLanguageServer, in2, out2);
		client.startListening();
		dafnyLanguageServer.setRemoteProxy(client.getRemoteProxy());
	}

	/**
	 * Get InputStream
	 */
	@Override
	public InputStream getInputStream() {
		return inputStream;
	}
	
	/**
	 * Get OutputStream 
	 */
	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Stoppt den Client und somit auch den Language Server
	 */
	@Override
	public void stop() {
		try {
			getInputStream().close();
			getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}