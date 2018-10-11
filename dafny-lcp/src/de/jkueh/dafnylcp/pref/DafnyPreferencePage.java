package de.jkueh.dafnylcp.pref;


import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;


/**
 * Repräsentiert die Dafny-Einstellungsseite
 * @author Jannik Kühnemundt
 *
 */
public class DafnyPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static String PAGE_ID = "de.jkueh.dafnylcplugin"; //$NON-NLS-1$

	/**
	 * Konstruktor
	 */
	public DafnyPreferencePage() {
		 super(GRID);
	}
	
	/**
	 * Erstellung der Eingabefelder für die Pfadangabe
	 */
	@Override
	protected void createFieldEditors() {
		if(System.getProperty("os.name").startsWith("Mac") || System.getProperty("os.name").startsWith("Linux")) {
			addField(new DirectoryFieldEditor("MONOPATH", "Path to Mono:", getFieldEditorParent()));
		}
		addField(new DirectoryFieldEditor("DAFNYPATH", "Path to DafnyServer.exe:", getFieldEditorParent()));
	}
	
	/**
	 * Initialisiert die Einstellungsseite
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, PAGE_ID));
        setDescription("Dafny Preference Page");
	}	
}
