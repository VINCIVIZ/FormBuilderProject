package formbuilder

import org.springframework.dao.DataIntegrityViolationException

class ClinicalVariableController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [clinicalVariableInstanceList: ClinicalVariable.list(params), clinicalVariableInstanceTotal: ClinicalVariable.count()]
    }

    def create() {
        [clinicalVariableInstance: new ClinicalVariable(params)]
    }

    def save() {
        def clinicalVariableInstance = new ClinicalVariable(params)
        if (!clinicalVariableInstance.save(flush: true)) {
            render(view: "create", model: [clinicalVariableInstance: clinicalVariableInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), clinicalVariableInstance.id])
        redirect(action: "show", id: clinicalVariableInstance.id)
    }

    def show() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        [clinicalVariableInstance: clinicalVariableInstance]
    }

    def edit() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        [clinicalVariableInstance: clinicalVariableInstance]
    }

    def update() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (clinicalVariableInstance.version > version) {
                clinicalVariableInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'clinicalVariable.label', default: 'ClinicalVariable')] as Object[],
                          "Another user has updated this ClinicalVariable while you were editing")
                render(view: "edit", model: [clinicalVariableInstance: clinicalVariableInstance])
                return
            }
        }

        clinicalVariableInstance.properties = params

        if (!clinicalVariableInstance.save(flush: true)) {
            render(view: "edit", model: [clinicalVariableInstance: clinicalVariableInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), clinicalVariableInstance.id])
        redirect(action: "show", id: clinicalVariableInstance.id)
    }

    def delete() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        try {
            clinicalVariableInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
