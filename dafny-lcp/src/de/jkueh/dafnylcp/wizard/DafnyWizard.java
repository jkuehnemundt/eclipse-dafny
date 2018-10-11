package de.jkueh.dafnylcp.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

/**
 * Wizard der ein Dafny-Projekt erstellt
 * @author Jannik Kühnemundt
 *
 */
public class DafnyWizard extends Wizard implements INewWizard {
	private DafnyWizardPage page;

	/**
	 * Konstruktor
	 */
	public DafnyWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Fügt eine Seite zum Wizard hinzu
	 */
	@Override
	public void addPages() {
		page = new DafnyWizardPage();
		addPage(page);
	}


	/**
	 * Diese Methode wird beim Klicken des Buttons 'Finish' aufgerufen
	 */
	@Override
	public boolean performFinish() {
		final String projectName = page.getProjectName();
		final String fileName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(projectName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			MessageDialog.openError(getShell(), "Error", e.getMessage());
			return false;
		}
		return true;
	}
	

	/**
	 * Erstellt das Projekt und eine Datei. Öffnet danach die Datei
	 * @param projectName Projektname
	 * @param fileName Dateiname
	 * @param monitor ProzessMonitor
	 * @throws CoreException
	 */
	private void doFinish(String projectName, String fileName, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project  = root.getProject(projectName);
		project.create(monitor);
		project.open(monitor);
		// Datei erstellen mit leerem Inhalt
		// Später könnte man über den Wizard Formular für Eingabe einer Klasse bzw. Methode erstellen
		final IFile file = project.getFile(new Path(fileName));
		byte[] bytes = "".getBytes();
		InputStream source = new ByteArrayInputStream(bytes);
		file.create(source, IResource.NONE, null);

		monitor.setTaskName("Opening file for editing...");
		//  Datei öffnen
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
}