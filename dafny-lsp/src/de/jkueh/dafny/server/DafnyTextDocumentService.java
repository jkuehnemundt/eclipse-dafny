package de.jkueh.dafny.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * Diese Klasse beinhaltet alle Methoden die Requests vom Client annehmen und Responses bilden.
 * @author Jannik Kühnemundt
 *
 */
public class DafnyTextDocumentService implements TextDocumentService {

	private final DafnyLanguageServer dafnyLanguageServer;
	
	/**
	 * Konstruktor 
	 * @param dafnyLanguageServer
	 */
	public DafnyTextDocumentService(DafnyLanguageServer dafnyLanguageServer) {
		this.dafnyLanguageServer = dafnyLanguageServer;	
	}

	@Override
	public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<Hover> hover(TextDocumentPositionParams position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<SignatureHelp> signatureHelp(TextDocumentPositionParams position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> definition(TextDocumentPositionParams position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(TextDocumentPositionParams position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends SymbolInformation>> documentSymbol(DocumentSymbolParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Command>> codeAction(CodeActionParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<CodeLens> resolveCodeLens(CodeLens unresolved) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Methode die beim Öffnen einer Datei aufgerufen wird und Diagnose-Daten an den Client sendet
	 */
	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		try {
			dafnyLanguageServer.getClient().publishDiagnostics(
				    new PublishDiagnosticsParams(params.getTextDocument().getUri(), validate(params.getTextDocument().getText(),params.getTextDocument().getUri()))
					);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}	
		
	}

	@Override
	public void didChange(DidChangeTextDocumentParams params) {
	    	
	}

	/**
	 * Methode wird beim Schließen einer Datei aufgerufen und beendet den DafnyProzess
	 */
	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		dafnyLanguageServer.getDafnyConnectionProvider().disconnect();
	}

	/**
	 * Methode die beim Speichern aufgerufen wird und Diagnose-Daten an den Client sendet
	 */
	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		try {
			dafnyLanguageServer.getClient().publishDiagnostics(
				    new PublishDiagnosticsParams(params.getTextDocument().getUri(), validate(params.getText(), params.getTextDocument().getUri()))
					);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}			
	}
	
	/**
	 * Ruft die sendData Methode auf und übergibt ihr Quellcode und den Dateinamen
	 * @param sourcecode Quellcode
	 * @param filename	Dateiname
	 * @return	eine Liste mit Diagnose Objekten
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private List<Diagnostic> validate(String sourcecode, String filename) throws InterruptedException, ExecutionException {
		return dafnyLanguageServer.getDafnyConnectionProvider().sendData(sourcecode,filename);
	       
	}


	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(TextDocumentPositionParams position) {
		return null;
	}


	
}
