Ext.define('FormBuilder.view.FieldSettingPanel', {
	
	extend:'Ext.panel.Panel',
	alias: 'widget.FieldSettingPanel',
	requires: ['FormBuilder.view.FieldForm'],
	
	bodyPadding: 0, 
	layout: 'anchor',
	border: 0,
	
	setFieldDesc : function(desc) {
		this.fieldDesc = desc;
		this.fieldForm = this.add({
			xtype: 'FieldForm',
		});
		
		this.fieldForm.setFieldDesc(desc);
		this.fieldForm.hide();
		this.drawPreview();
	},
	
	refreshSettings : function(caption, description) {
		this.fieldDesc.caption = caption;
		this.fieldDesc.description = description;
		this.fieldForm.hide();
		this.drawPreview();
		this.doLayout();
	},
	
	drawPreview : function() {
		this.previewPanel = this.add({
			xtype: 'form',
			layout: 'anchor',
			border: 0,
			bodyPadding: 10,
			fieldDefaults : {
				labelWidth: 150,
			},
			listeners : {
				mouseover : {
					element : 'body',
					fn : function() {
						this.setStyle('background-color', '#fffae1');
					}
				},
				mouseout : {
					element : 'body',
					fn : function() {
						this.setStyle('background-color', null);
					}
				},
				dblclick : {
					element : 'body',
					fn : this.editField,
					scope : this,
				} 
			}
		});
		
		dataType = this.getDataType();
		if (Ext.isObject(dataType)) { //radio buttons
			items = [];
			for (var key in dataType) {
				item = {
					name: this.fieldDesc.fieldName,
					boxLabel: dataType[key],
					inputValue: key
				};
				items.push(item);
			}
			
			this.previewPanel.add({
				xtype: 'radiogroup',
				fieldLabel: this.fieldDesc.caption,
				vertical: true,
				columns: 1,
				items: items,
			});
			
		} else if (dataType == 'TextClinicalVariable') {
			this.previewPanel.add({
				xtype: 'textfield',
				fieldLabel: this.fieldDesc.caption,
				labelWidth: 150,
			});
		} else if (dataType == 'BooleanClinicalVariable') {
			alert('Unimplemented ' + dataType);
		} else if (dataType == 'IntegerClinicalVariable') {
			alert('Unimplemented ' + dataType);
		} else if (dataType == 'DoubleClinicalVariable') {
			alert('Unimplemented ' + dataType);
		} else if (dataType == 'TimeClinicalVariable') {
			alert('Unimplemented ' + dataType);
		} else if (dataType == 'DateClinicalVariable') {
			alert('Unimplemented ' + dataType);
		} else if (dataType == 'ComplexClinicalVariable') {
			alert('Unimplemented ' + dataType);
		} else {
			alert('Unknown datatype: ' + dataType);
		}
		
    	if (this.fieldDesc.description != null && this.fieldDesc.description.length > 0) {
			Ext.create('Ext.tip.ToolTip', {
				target: this.previewPanel.el,
				html: this.fieldDesc.description
			});
		}
	},
	
	getDataType : function() {
		if (this.fieldDesc.className == 'ComplexClinicalVariable') {
			if (this.fieldDesc.members != null && this.fieldDesc.members.length > 0) {
				return this.fieldDesc.className;
			} else if (Ext.isObject(this.fieldDesc.enumValues) && Object.keys(this.fieldDesc.enumValues).length > 0) {
				return this.fieldDesc.enumValues;
			} else {
				return 'TextClinicalVariable';
			}
		} else {
			return this.fieldDesc.className;
		}
	},
	
	editField : function() {
		this.fieldForm.show();
		this.remove(this.previewPanel);
	},
	
	removeVariable : function() {
		this.hide();
		this.up('VariableFieldSet').addRemovedField(this.fieldDesc.caption, this.id);
	}
});
