Ext.define('FormBuilder.model.CommonType', {
	requires: 'FormBuilder.model.CommonTypeField',
	extend: 'Ext.data.Model',
	fields: ['name', 'description', 'fields'],
	
	proxy: {
		type: 'ajax',
		url: 'data/CommonTypes.json',
		reader: {
			type: 'json',
			root: 'results',
		}
	}
});
