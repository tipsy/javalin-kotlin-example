package app

import app.user.User
import app.user.UserDao
import io.javalin.ApiBuilder.*
import io.javalin.Javalin

fun main(args: Array<String>) {

    val userDao = UserDao()

    val app = Javalin.create().apply {
        port(7000)
        exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start()

    app.routes {

        get("/users") { ctx ->
            ctx.json(userDao.users)
        }

        get("/users/:id") { ctx ->
            ctx.json(userDao.findById(ctx.param("id")!!.toInt())!!)
        }

        get("/users/email/:email") { ctx ->
            ctx.json(userDao.findByEmail(ctx.param("email")!!)!!)
        }

        post("/users/create") { ctx ->
            val user = ctx.bodyAsClass(User::class.java)
            userDao.save(name = user.name, email = user.email)
            ctx.status(201)
        }

        patch("/users/update/:id") { ctx ->
            val user = ctx.bodyAsClass(User::class.java)
            userDao.update(
                    id = ctx.param("id")!!.toInt(),
                    user = user
            )
            ctx.status(204)
        }

        delete("/users/delete/:id") { ctx ->
            userDao.delete(ctx.param("id")!!.toInt())
            ctx.status(204)
        }

    }

}
