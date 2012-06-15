Ext.define('FormBuilder.model.Schema', {
	extend: 'Ext.data.Model',
	fields: ['name'],
	
	proxy: {
		type: 'ajax',
		url: 'Schema/list',
		reader: {
			type: 'json',
		}
	}
});
