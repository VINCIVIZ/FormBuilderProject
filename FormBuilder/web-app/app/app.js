

Ext.Loader.setConfig({enabled:true});

Ext.application({
    name: 'FormBuilder',
    
    autoCreateViewport: true,
    
    models: ['CommonType', 'FormEditor', 'Schema'],
    stores: ['CommonType', 'Schema'],
    controllers: ['CommonTypeList', 'FormEditor', 'SchemaList']
});

