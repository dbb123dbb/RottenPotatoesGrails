package com.dennisbeasley.RottenPotatoes

import org.springframework.dao.DataIntegrityViolationException

class MoviesController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [moviesInstanceList: Movies.list(params), moviesInstanceTotal: Movies.count()]
    }

    def create() {
        [moviesInstance: new Movies(params)]
    }

    def save() {
        def moviesInstance = new Movies(params)
        if (!moviesInstance.save(flush: true)) {
            render(view: "create", model: [moviesInstance: moviesInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'movies.label', default: 'Movies'), moviesInstance.id])
        redirect(action: "show", id: moviesInstance.id)
    }

    def show() {
        def moviesInstance = Movies.get(params.id)
        if (!moviesInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'movies.label', default: 'Movies'), params.id])
            redirect(action: "list")
            return
        }

        [moviesInstance: moviesInstance]
    }

    def edit() {
        def moviesInstance = Movies.get(params.id)
        if (!moviesInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'movies.label', default: 'Movies'), params.id])
            redirect(action: "list")
            return
        }

        [moviesInstance: moviesInstance]
    }

    def update() {
        def moviesInstance = Movies.get(params.id)
        if (!moviesInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'movies.label', default: 'Movies'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (moviesInstance.version > version) {
                moviesInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'movies.label', default: 'Movies')] as Object[],
                          "Another user has updated this Movies while you were editing")
                render(view: "edit", model: [moviesInstance: moviesInstance])
                return
            }
        }

        moviesInstance.properties = params

        if (!moviesInstance.save(flush: true)) {
            render(view: "edit", model: [moviesInstance: moviesInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'movies.label', default: 'Movies'), moviesInstance.id])
        redirect(action: "show", id: moviesInstance.id)
    }

    def delete() {
        def moviesInstance = Movies.get(params.id)
        if (!moviesInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'movies.label', default: 'Movies'), params.id])
            redirect(action: "list")
            return
        }

        try {
            moviesInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'movies.label', default: 'Movies'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'movies.label', default: 'Movies'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
