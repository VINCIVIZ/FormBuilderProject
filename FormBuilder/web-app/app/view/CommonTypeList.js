Ext.define('FormBuilder.view.CommonTypeList', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.CommonTypeList',
	
	requires: 'FormBuilder.store.CommonType',
	store: 'CommonType',
	
	title: 'Common Types',
	
	columns: [{
		header: 'Element',
		dataIndex: 'name',
		flex: 1
	}],
	
	viewConfig: {
		plugins: {
			ptype: 'gridviewdragdrop',
			dragText: 'Drag and drop in Form Editor to insert new element. ',
			enableDrop: false,
			dragGroup: 'elementDdGroup'
		}
	}
});

