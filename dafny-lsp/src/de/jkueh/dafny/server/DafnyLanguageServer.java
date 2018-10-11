package de.jkueh.dafny.server;

import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import de.jkueh.dafny.core.DafnyConnectionProvider;

/**
 * Repräsentiert einen LanguageServer für Dafny
 * @author Jannik Kühnemundt
 *
 */
public class DafnyLanguageServer implements LanguageServer {

	private DafnyTextDocumentService textService;
	private DafnyWorkspaceService workspaceService;
	private LanguageClient client;
	private DafnyConnectionProvider dafnyconnectionprovider;

	/**
	 * Konstruktor
	 */
	public DafnyLanguageServer() {
		textService = new DafnyTextDocumentService(this);
		workspaceService = new DafnyWorkspaceService();
	}
	
	/**
	 * Initialisierung, Benachrichtigung an den Client, was der Server unterstützt
	 */
	public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
		dafnyconnectionprovider = new DafnyConnectionProvider();
		InitializeResult res = new InitializeResult(new ServerCapabilities());
		res.getCapabilities().setCodeActionProvider(Boolean.FALSE);
		res.getCapabilities().setDefinitionProvider(Boolean.FALSE);
		res.getCapabilities().setHoverProvider(Boolean.FALSE);
		res.getCapabilities().setReferencesProvider(Boolean.FALSE);
		res.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
        return CompletableFuture.supplyAsync(() -> res);
	}

	/**
	 * Fährt den Server herunter
	 */
	public CompletableFuture<Object> shutdown() {
		return CompletableFuture.supplyAsync(() -> Boolean.TRUE);
	}
	
	@Override
	public void exit() {
		// TODO Auto-generated method stub
	}

	/**
	 * Getter für TextDocumentService
	 */
	public TextDocumentService getTextDocumentService() {
		return this.textService;
	}

	/**
	 * Getter für Workspace
	 */
	public WorkspaceService getWorkspaceService() {
		return this.workspaceService;
	}

	/**
	 * Setzt den Client fest
	 * @param remoteProxy Client mit dem sich verbunden wird
	 */
	public void setRemoteProxy(LanguageClient remoteProxy) {
		this.client = remoteProxy;
	}
	
	/**
	 * Getter für DafnyConnectionProvider
	 * @return DafnyConnectionProvider
	 */
	public DafnyConnectionProvider getDafnyConnectionProvider() {
		return this.dafnyconnectionprovider;
	}
	
	/**
	 * Getter für den Client
	 * @return Client
	 */
	public LanguageClient getClient() {
			return client;
	}


}
