Ext.define('FormBuilder.view.CommonTypeList', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.CommonTypeList',
	
	requires: 'FormBuilder.store.CommonType',
	store: 'CommonType',
	
	title: 'Common Types',
	
	columns: [{
		dataIndex: 'name',
		flex: 1
	}],
});
