package de.jkueh.dafnylcp.wizard;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * Eine Wizard-Seite
 * @author Jannik Kühnemundt
 *
 */
public class DafnyWizardPage extends WizardPage {
	// Name des Projekts
	private Text projectText;
	// Name der Datei
	private Text fileText;

	/**
	 * Konstruktor der WizardPage
	 * 
	 */
	public DafnyWizardPage() {
		super("wizardPage");
		setTitle("Create a Dafny Project");
		setDescription("This wizard creates a Dafny project");
	}

	/**
	 * Gestaltung des Wizards
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Project name:");

		projectText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectText.setLayoutData(gd);
		projectText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&File name:");

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		projectText.setText("Dafny Project");
		fileText.setText("new_file.dfy");
		dialogChanged();
		setControl(container);
	}

	/**
	 * Stellt sicher, ob die Bedingungen zur Projekterstellungen erfüllt sind.
	 */
	private void dialogChanged() {
		String fileName = getFileName();
		// Projekt muss einen Namen besitzen
		if (getProjectName().length() == 0) {
			updateStatus("Project name must be specified");
			return;
		}
		// Projektname schon vorhanden
		if(ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName()).exists()) {
			updateStatus("Project name must be specified");
			return;
		}
		// Dateiname muss einen Namen besitzen
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("dfy") == false) {
				updateStatus("File extension must be \"dfy\"");
				return;
			}
		}
		updateStatus(null);
	}

	/**
	 * Gibt bei Übergabe eines String, den String in Form einer Fehlermeldung aus
	 * @param message Fehlermeldung
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	/**
	 * Gibt den Projektnamen aus
	 * @return Projektnamen
	 */
	public String getProjectName() {
		return projectText.getText();
	}

	/**
	 * Gibt den Dateinamen zurück
	 * @return Dateiname
	 */
	public String getFileName() {
		return fileText.getText();
	}
}