package org.deniz.secureapi


import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invoices")
class InvoiceController {
    val invoices: MutableList<Invoice> = mutableListOf()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(SecurityConfig.ROLE_CLIENT)
    fun addInvoice(@RequestBody invoice: Invoice) { invoices.add(Invoice(invoice.id, invoice.price)) }

    @Secured(SecurityConfig.ROLE_CLIENT)
    @GetMapping
    fun getAllInvoices() = invoices

    @GetMapping("/{id}")
    fun getInvoice(@PathVariable id: Long) = invoices.singleOrNull { s -> s.id == id }

    @DeleteMapping("/{name}")
    fun deleteInvoice(@PathVariable id: Long) { invoices.removeIf { s -> s.id == id } }
}

data class Invoice(val id: Long, val price: Long)
