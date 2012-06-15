Ext.define('FormBuilder.view.SchemaList', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.SchemaList',
	
	requires: 'FormBuilder.store.Schema',
	store: 'Schema',
	
	title: 'Saved Schemas',
	
	columns: [{
		header: 'Schema',
		dataIndex: 'name',
		flex: 1
	}],
});

