Ext.define('FormBuilder.view.FormEditor', {
	extend:'Ext.panel.Panel',
	alias: 'widget.FormEditor',
	
    requires: [
		'FormBuilder.view.VariableFieldSet',
	],
	
	bodyPadding: 10, 
	autoScroll: true,
	 
	title: 'Form Editor', 
	layout: 'anchor',
	
	defaultType: 'textfield',
	fieldDefaults: {
		//labelAlign: 'top'
	}
});
