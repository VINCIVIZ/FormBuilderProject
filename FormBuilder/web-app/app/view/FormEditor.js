Ext.define('FormBuilder.view.FormEditor', {
	extend:'Ext.form.Panel',
	alias: 'widget.FormEditor',
	bodyPadding: 10, 
	autoScroll: true,
	 
	title: 'Form Editor', 
	layout: 'anchor',
	
	defaultType: 'textfield',
	fieldDefaults: {
		//labelAlign: 'top'
	}
});
