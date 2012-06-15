Ext.define('FormBuilder.view.FieldForm', {
	extend:'Ext.form.Panel',
	alias: 'widget.FieldForm',
	bodyPadding: 10, 
	layout: 'anchor',
	
	defaultType: 'textfield',
	fieldDefaults: {
		anchor: '80%',
	},
	
	items: [{
		xtype: 'displayfield',
		fieldLabel: 'Variable',
		name: 'varName',
	}, {
		xtype: 'displayfield',
		fieldLabel: 'Data Type',
		name: 'varType',
	}, {
		fieldLabel: 'Question Title',
		allowBlank: false,
		name: 'caption',
	}, {
		fieldLabel: 'Description',
		allowBlank: true,
		name: 'description',
	}],
	
	buttons: [{
		text: 'Done',
		formBind: true,
		handler: function() {
			var form = this.up('form').getForm();
			this.up('FieldSettingPanel').refreshSettings(
				form.findField('caption').getValue(),
				form.findField('description').getValue()
			);
		},
		itemId: 'doneButton',
	}, {
		text: 'Remove', 
		handler : function() {
			this.up('FieldSettingPanel').removeVariable();
		},
		itemId: 'removeButton',
	}],
	
	buttonAlign: 'left',
	
	setFieldDesc : function(desc) {
		this.getForm().findField('caption').setValue(desc.caption);
		this.getForm().findField('description').setValue(desc.description);
		this.getForm().findField('varName').setValue(desc.fieldName);
		this.query('#removeButton')[0].setDisabled(!desc.optional);
		
		var dataType = this.up('FieldSettingPanel').getDataType();
		if (Ext.isObject(dataType)) {
			values = [];
			for (v in dataType) {
				values.push(dataType[v]);
			}
			this.getForm().findField('varType').setValue('Enum: ' + values.toString());
		} else {
			this.getForm().findField('varType').setValue(dataType.substr(0, dataType.length - 16)); 	// 16 == 'ClinicalVariable'.length
		}
	}
});
