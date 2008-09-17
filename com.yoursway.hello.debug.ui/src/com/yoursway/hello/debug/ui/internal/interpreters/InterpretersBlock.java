/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.debug.ui.internal.interpreters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersMessages;
import org.eclipse.dltk.internal.ui.util.SWTUtil;
import org.eclipse.dltk.internal.ui.util.TableLayoutComposite;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.InterpreterStandin;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.yoursway.hello.core.HelloNature;

/**
 * A composite that displays installed InterpreterEnvironment's in a table.
 * InterpreterEnvironments can be added, removed, edited, and searched for.
 * <p>
 * This block implements ISelectionProvider - it sends selection change events
 * when the checked InterpreterEnvironment in the table changes, or when the
 * "use default" button check state changes.
 * </p>
 */
public class InterpretersBlock implements
		IAddInterpreterDialogRequestor, ISelectionProvider {

	/**
	 * This block's control
	 */
	private Composite fControl;

	/**
	 * Interpreters being displayed
	 */
	protected List<IInterpreterInstall> fInterpreters = new ArrayList<IInterpreterInstall>();

	/**
	 * The main list control
	 */
	protected CheckboxTableViewer fInterpreterList;

	// Action buttons
	private Button fAddButton;
	private Button fRemoveButton;
	private Button fEditButton;

	// index of column used for sorting
	private int fSortColumn = 0;

	/**
	 * Selection listeners (checked InterpreterEnvironment changes)
	 */
	private ListenerList fSelectionListeners = new ListenerList();

	/**
	 * Previous selection
	 */
	private ISelection fPrevSelection = new StructuredSelection();

	private Table fTable;

	/**
	 * Content provider to show a list of InterpreterEnvironments
	 */
	class InterpretersContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object input) {
			return fInterpreters.toArray();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}
	}

	/**
	 * Label provider for installed InterpreterEnvironments table.
	 */
	class InterpreterLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		/**
		 * @see ITableLabelProvider#getColumnText(Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof IInterpreterInstall) {
				IInterpreterInstall interp = (IInterpreterInstall) element;
				switch (columnIndex) {
				case 0:
					return interp.getName();
				case 1:
					return interp.getInterpreterInstallType().getName();
				case 2:
					return interp.getRawInstallLocation().toString();
				}
			}
			return element.toString();
		}

		/**
		 * @see ITableLabelProvider#getColumnImage(Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				// TODO: instert interpreter logo here
			}
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		return new StructuredSelection(fInterpreterList.getCheckedElements());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		fSelectionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if (!selection.equals(fPrevSelection)) {
				fPrevSelection = selection;
				Object interp = ((IStructuredSelection) selection)
						.getFirstElement();
				if (interp == null) {
					fInterpreterList.setCheckedElements(new Object[0]);
				} else {
					fInterpreterList
							.setCheckedElements(new Object[] { interp });
					fInterpreterList.reveal(interp);
				}
				fireSelectionChanged();
			}
		}
	}

	/**
	 * Creates this block's control in the given control.
	 * 
	 * @param ancestor
	 *            containing control
	 * @param useManageButton
	 *            whether to present a single 'manage...' button to the user
	 *            that opens the installed InterpreterEnvironments pref page for
	 *            InterpreterEnvironment management, or to provide 'add, remove,
	 *            edit, and search' buttons.
	 */
	public void createControl(Composite ancestor) {

		Composite parent = new Composite(ancestor, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		Font font = ancestor.getFont();
		parent.setFont(font);
		parent.setLayout(layout);

		fControl = parent;

		GridData data;

		Label tableLabel = new Label(parent, SWT.NONE);
		tableLabel.setText(InterpretersMessages.InstalledInterpretersBlock_15);
		data = new GridData();
		data.horizontalSpan = 2;
		tableLabel.setLayoutData(data);
		tableLabel.setFont(font);

		PixelConverter conv = new PixelConverter(parent);
		data = new GridData(GridData.FILL_BOTH);
		data.widthHint = conv.convertWidthInCharsToPixels(50);
		TableLayoutComposite tblComposite = new TableLayoutComposite(parent,
				SWT.NONE);
		tblComposite.setLayoutData(data);
		fTable = new Table(tblComposite, SWT.CHECK | SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION);

		data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 350;
		fTable.setLayoutData(data);
		fTable.setFont(font);

		fTable.setHeaderVisible(true);
		fTable.setLinesVisible(true);

		TableColumn column1 = new TableColumn(fTable, SWT.NULL);
		column1.setText(InterpretersMessages.InstalledInterpretersBlock_0);
		column1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sortByName();
			}
		});
		tblComposite.addColumnData(new ColumnPixelData(70));

		TableColumn column2 = new TableColumn(fTable, SWT.NULL);
		column2.setText(InterpretersMessages.InstalledInterpretersBlock_2);
		column2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sortByType();
			}
		});
		tblComposite.addColumnData(new ColumnPixelData(70));

		TableColumn column3 = new TableColumn(fTable, SWT.NULL);
		column3.setText(InterpretersMessages.InstalledInterpretersBlock_1);
		column3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sortByLocation();
			}
		});
		tblComposite.addColumnData(new ColumnPixelData(150));

		fInterpreterList = new CheckboxTableViewer(fTable);
		fInterpreterList.setLabelProvider(new InterpreterLabelProvider());
		fInterpreterList.setContentProvider(new InterpretersContentProvider());
		// by default, sort by name
		sortByName();

		fInterpreterList
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent evt) {
						enableButtons();
					}
				});

		fInterpreterList.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					setCheckedInterpreter((IInterpreterInstall) event
							.getElement());
				} else {
					setCheckedInterpreter(null);
				}
			}
		});

		fInterpreterList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent e) {
				if (!fInterpreterList.getSelection().isEmpty()) {
					editInterpreter();
				}
			}
		});
		fTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.character == SWT.DEL && event.stateMask == 0) {
					if (fRemoveButton.getEnabled())
						removeInterpreters();
				}
			}
		});

		Composite buttons = new Composite(parent, SWT.NULL);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);
		buttons.setFont(font);

		fAddButton = createPushButton(buttons,
				InterpretersMessages.InstalledInterpretersBlock_3);
		fAddButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event evt) {
				addInterpreter();
			}
		});

		fEditButton = createPushButton(buttons,
				InterpretersMessages.InstalledInterpretersBlock_4);
		fEditButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event evt) {
				editInterpreter();
			}
		});

		fRemoveButton = createPushButton(buttons,
				InterpretersMessages.InstalledInterpretersBlock_5);
		fRemoveButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event evt) {
				removeInterpreters();
			}
		});

		// copied from ListDialogField.CreateSeparator()
		Label separator = new Label(buttons, SWT.NONE);
		separator.setVisible(false);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.heightHint = 4;
		separator.setLayoutData(gd);

		fillWithWorkspaceInterpreters();
		
		enableButtons();
		fAddButton.setEnabled(ScriptRuntime
				.getInterpreterInstallTypes(getCurrentNature()).length > 0);
	}

	/**
	 * Fire current selection
	 */
	private void fireSelectionChanged() {
		SelectionChangedEvent event = new SelectionChangedEvent(this,
				getSelection());
		Object[] listeners = fSelectionListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			ISelectionChangedListener listener = (ISelectionChangedListener) listeners[i];
			listener.selectionChanged(event);
		}
	}

	/**
	 * Sorts by Interpreter type, and name within type.
	 */
	private void sortByType() {
		fInterpreterList.setSorter(new ViewerSorter() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				if ((e1 instanceof IInterpreterInstall)
						&& (e2 instanceof IInterpreterInstall)) {
					IInterpreterInstall left = (IInterpreterInstall) e1;
					IInterpreterInstall right = (IInterpreterInstall) e2;
					String leftType = left.getInterpreterInstallType()
							.getName();
					String rightType = right.getInterpreterInstallType()
							.getName();
					int res = leftType.compareToIgnoreCase(rightType);
					if (res != 0) {
						return res;
					}
					return left.getName().compareToIgnoreCase(right.getName());
				}
				return super.compare(viewer, e1, e2);
			}

			public boolean isSorterProperty(Object element, String property) {
				return true;
			}
		});
		fSortColumn = 3;
	}

	/**
	 * Sorts by Interpreter name.
	 */
	private void sortByName() {
		fInterpreterList.setSorter(new ViewerSorter() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				if ((e1 instanceof IInterpreterInstall)
						&& (e2 instanceof IInterpreterInstall)) {
					IInterpreterInstall left = (IInterpreterInstall) e1;
					IInterpreterInstall right = (IInterpreterInstall) e2;
					return left.getName().compareToIgnoreCase(right.getName());
				}
				return super.compare(viewer, e1, e2);
			}

			public boolean isSorterProperty(Object element, String property) {
				return true;
			}
		});
		fSortColumn = 1;
	}

	/**
	 * Sorts by Interpreter location.
	 */
	private void sortByLocation() {
		fInterpreterList.setSorter(new ViewerSorter() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				if ((e1 instanceof IInterpreterInstall)
						&& (e2 instanceof IInterpreterInstall)) {
					IInterpreterInstall left = (IInterpreterInstall) e1;
					IInterpreterInstall right = (IInterpreterInstall) e2;
					return left.getInstallLocation().toString()
							.compareToIgnoreCase(
									right.getInstallLocation().toString());
				}
				return super.compare(viewer, e1, e2);
			}

			public boolean isSorterProperty(Object element, String property) {
				return true;
			}
		});
		fSortColumn = 2;
	}

	private void enableButtons() {
		IStructuredSelection selection = (IStructuredSelection) fInterpreterList
				.getSelection();
		int selectionCount = selection.size();
		fEditButton.setEnabled(selectionCount == 1);
		if (selectionCount > 0) {
			Iterator iterator = selection.iterator();
			while (iterator.hasNext()) {
				IInterpreterInstall install = (IInterpreterInstall) iterator
						.next();
				if (isContributed(install)) {
					fRemoveButton.setEnabled(false);
					return;
				}
			}
			fRemoveButton.setEnabled(true);
		} else {
			fRemoveButton.setEnabled(false);
		}
	}

	protected Button createPushButton(Composite parent, String label) {
		return SWTUtil.createPushButton(parent, label, null);
	}

	private boolean isContributed(IInterpreterInstall install) {
		return ScriptRuntime.isContributedInterpreterInstall(install.getId());
	}

	/**
	 * Returns this block's control
	 * 
	 * @return control
	 */
	public Control getControl() {
		return fControl;
	}

	/**
	 * Sets the InterpreterEnvironments to be displayed in this block
	 * 
	 * @param Interpreters
	 *            InterpreterEnvironments to be displayed
	 */
	protected void setInterpreters(IInterpreterInstall[] Interpreters) {
		fInterpreters.clear();
		for (int i = 0; i < Interpreters.length; i++) {
			fInterpreters.add(Interpreters[i]);
		}
		fInterpreterList.setInput(fInterpreters);
		fInterpreterList.refresh();
	}

	/**
	 * Returns the InterpreterEnvironments currently being displayed in this
	 * block
	 * 
	 * @return InterpreterEnvironments currently being displayed in this block
	 */
	public IInterpreterInstall[] getInterpreters() {
		return fInterpreters
				.toArray(new IInterpreterInstall[fInterpreters.size()]);
	}

	/**
	 * @see IAddInterpreterDialogRequestor#isDuplicateName(String)
	 */
	public boolean isDuplicateName(String name) {
		for (int i = 0; i < fInterpreters.size(); i++) {
			IInterpreterInstall Interpreter = fInterpreters
					.get(i);
			if (Interpreter.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private void removeInterpreters() {
		IStructuredSelection selection = (IStructuredSelection) fInterpreterList
				.getSelection();
		IInterpreterInstall[] Interpreters = new IInterpreterInstall[selection
				.size()];
		Iterator iter = selection.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Interpreters[i] = (IInterpreterInstall) iter.next();
			i++;
		}
		removeInterpreters(Interpreters);
	}

	/**
	 * Removes the given Interpreters from the table.
	 * 
	 * @param Interpreters
	 */
	public void removeInterpreters(IInterpreterInstall[] Interpreters) {
		IStructuredSelection prev = (IStructuredSelection) getSelection();
		for (int i = 0; i < Interpreters.length; i++) {
			fInterpreters.remove(Interpreters[i]);
		}
		fInterpreterList.refresh();
		IStructuredSelection curr = (IStructuredSelection) getSelection();
		if (!curr.equals(prev)) {
			IInterpreterInstall[] installs = getInterpreters();
			if (curr.size() == 0 && installs.length == 1) {
				// pick a default Interpreter automatically
				setSelection(new StructuredSelection(installs[0]));
			} else {
				fireSelectionChanged();
			}
		}
	}

	protected Shell getShell() {
		return getControl().getShell();
	}

	/**
	 * Sets the checked InterpreterEnvironment, possible <code>null</code>
	 * 
	 * @param Interpreter
	 *            InterpreterEnvironment or <code>null</code>
	 */
	public void setCheckedInterpreter(IInterpreterInstall Interpreter) {
		if (Interpreter == null) {
			setSelection(new StructuredSelection());
		} else {
			setSelection(new StructuredSelection(Interpreter));
		}
	}

	/**
	 * Returns the checked Interpreter or <code>null</code> if none.
	 * 
	 * @return the checked Interpreter or <code>null</code> if none
	 */
	public IInterpreterInstall getCheckedInterpreter() {
		Object[] objects = fInterpreterList.getCheckedElements();
		if (objects.length == 0) {
			return null;
		}
		return (IInterpreterInstall) objects[0];
	}

	/**
	 * Persist table settings into the give dialog store, prefixed with the
	 * given key.
	 * 
	 * @param settings
	 *            dialog store
	 * @param qualifier
	 *            key qualifier
	 */
	public void saveColumnSettings(IDialogSettings settings, String qualifier) {
		int columnCount = fTable.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			settings
					.put(
							qualifier + ".columnWidth" + i, fTable.getColumn(i).getWidth()); //$NON-NLS-1$
		}
		settings.put(qualifier + ".sortColumn", fSortColumn); //$NON-NLS-1$
	}

	/**
	 * Restore table settings from the given dialog store using the given key.
	 * 
	 * @param settings
	 *            dialog settings store
	 * @param qualifier
	 *            key to restore settings from
	 */
	public void restoreColumnSettings(IDialogSettings settings, String qualifier) {
		fInterpreterList.getTable().layout(true);
		restoreColumnWidths(settings, qualifier);
		try {
			fSortColumn = settings.getInt(qualifier + ".sortColumn"); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			fSortColumn = 1;
		}
		switch (fSortColumn) {
		case 1:
			sortByName();
			break;
		case 2:
			sortByLocation();
			break;
		case 3:
			sortByType();
			break;
		}
	}

	private void restoreColumnWidths(IDialogSettings settings, String qualifier) {
		int columnCount = fTable.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			int width = -1;

			try {
				width = settings.getInt(qualifier + ".columnWidth" + i); //$NON-NLS-1$
			} catch (NumberFormatException e) {
			}

			if (width <= 0) {
				fTable.getColumn(i).pack();
			} else {
				fTable.getColumn(i).setWidth(width);
			}
		}
	}

	/**
	 * Populates the InterpreterEnvironment table with existing
	 * InterpreterEnvironments defined in the workspace.
	 */
	protected void fillWithWorkspaceInterpreters() {
		// fill with interpreters
		List<InterpreterStandin> standins = new ArrayList<InterpreterStandin>();
		IInterpreterInstallType[] types = ScriptRuntime
				.getInterpreterInstallTypes(getCurrentNature());
		for (int i = 0; i < types.length; i++) {
			IInterpreterInstallType type = types[i];
			IInterpreterInstall[] installs = type.getInterpreterInstalls();
			if (installs != null)
				for (int j = 0; j < installs.length; j++) {
					IInterpreterInstall install = installs[j];
					standins.add(new InterpreterStandin(install));
				}
		}
		setInterpreters(standins
				.toArray(new IInterpreterInstall[standins.size()]));
	}

	public void interpreterAdded(IInterpreterInstall Interpreter) {
		fInterpreters.add(Interpreter);
		fInterpreterList.refresh();

		IInterpreterInstall[] installs = getInterpreters();
		if (installs.length == 1) {
			// pick a default Interpreter automatically
			setSelection(new StructuredSelection(installs[0]));
		}
		fireSelectionChanged();
	}

	// Make sure that InterpreterStandin ids are unique if multiple calls to
	// System.currentTimeMillis()
	// happen very quickly
	private static String fgLastUsedID;

	/**
	 * Find a unique Interpreter id. Check existing 'real' Interpreters, as well
	 * as the last id used for a InterpreterStandin.
	 */
	protected String createUniqueId(IInterpreterInstallType InterpreterType) {
		String id = null;
		do {
			id = String.valueOf(System.currentTimeMillis());
		} while (InterpreterType.findInterpreterInstall(id) != null
				|| id.equals(fgLastUsedID));
		fgLastUsedID = id;
		return id;
	}

	/**
	 * Compares the given name against current names and adds the appropriate
	 * numerical suffix to ensure that it is unique.
	 * 
	 * @param name
	 *            the name with which to ensure uniqueness
	 * @return the unique version of the given name
	 * 
	 */
	protected String generateName(String name) {
		if (!isDuplicateName(name)) {
			return name;
		}

		if (name.matches(".*\\(\\d*\\)")) { //$NON-NLS-1$
			int start = name.lastIndexOf('(');
			int end = name.lastIndexOf(')');
			String stringInt = name.substring(start + 1, end);
			int numericValue = Integer.parseInt(stringInt);
			String newName = name.substring(0, start + 1) + (numericValue + 1)
					+ ")"; //$NON-NLS-1$
			return generateName(newName);
		} else {
			return generateName(name + " (1)"); //$NON-NLS-1$
		}
	}


	protected void copyInterpreter() {
		IStructuredSelection selection = (IStructuredSelection) fInterpreterList
				.getSelection();
		Iterator it = selection.iterator();

		ArrayList<InterpreterStandin> newEntries = new ArrayList<InterpreterStandin>();
		while (it.hasNext()) {
			IInterpreterInstall selectedInterpreter = (IInterpreterInstall) it
					.next();

			// duplicate & add Interpreter
			InterpreterStandin standin = new InterpreterStandin(
					selectedInterpreter, createUniqueId(selectedInterpreter
							.getInterpreterInstallType()));
			standin.setName(generateName(selectedInterpreter.getName()));
			AddScriptInterpreterDialog dialog = createInterpreterDialog(standin);
			dialog.setTitle(InterpretersMessages.InstalledInterpretersBlock_18);
			if (dialog.open() != Window.OK) {
				return;
			}
			newEntries.add(standin);
			fInterpreters.add(standin);
		}
		fInterpreterList.refresh();
		fInterpreterList.setSelection(new StructuredSelection(newEntries
				.toArray()));
	}

	/**
	 * Bring up a dialog that lets the user create a new Interpreter definition.
	 */
	protected void addInterpreter() {
		AddScriptInterpreterDialog dialog = createInterpreterDialog(null);
		dialog.setTitle(InterpretersMessages.InstalledInterpretersBlock_7);
		if (dialog.open() != Window.OK) {
			return;
		}
		fInterpreterList.refresh();
	}

	protected void editInterpreter() {
		IStructuredSelection selection = (IStructuredSelection) fInterpreterList
				.getSelection();
		IInterpreterInstall install = (IInterpreterInstall) selection
				.getFirstElement();

		if (install == null) {
			return;
		}

		AddScriptInterpreterDialog dialog = createInterpreterDialog(install);
		dialog.setTitle(InterpretersMessages.InstalledInterpretersBlock_8);
		if (dialog.open() != Window.OK) {
			return;
		}
		fInterpreterList.refresh(install);
	}
	
	protected AddScriptInterpreterDialog createInterpreterDialog(IInterpreterInstall standin) {
		HelloInterpreterDialog dialog = new HelloInterpreterDialog(this, 
				getShell(), ScriptRuntime.getInterpreterInstallTypes(getCurrentNature()), 
				standin);
		return dialog;
	}

	protected String getCurrentNature() {
		return HelloNature.NATURE_ID;
	}
	
}
