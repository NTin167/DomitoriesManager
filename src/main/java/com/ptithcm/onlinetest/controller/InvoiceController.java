package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.payload.dto.InvoiceDTO;
import com.ptithcm.onlinetest.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // GET /invoices/{id}
    @GetMapping("/getInvoiceByStudentID/{id}")
    public ResponseEntity<?> getInvoiceByStudentId(@PathVariable Long id) {
        List<InvoiceDTO> invoiceDTO = invoiceService.getAllInvoicesByStudentId(id);
        if (invoiceDTO != null) {
            return ResponseEntity.ok(invoiceDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    // POST /invoices
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO createdInvoice = invoiceService.createInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    // PUT /invoices/{id}
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
        if (updatedInvoice != null) {
            return ResponseEntity.ok(updatedInvoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /invoices/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        boolean deleted = invoiceService.deleteInvoice(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<?> paymentByInvoiceId(@PathVariable Long id) {
        int i = invoiceService.paymentByInvoiceId(id);
        switch (i) {
            case 0:
                return new ResponseEntity<>("Thanh toán thấy bại vì hóa đơn này đã được thanh toán! ", HttpStatus.ACCEPTED);
            case 1:
                return new ResponseEntity<>("Thanh toán hóa đơn thành công", HttpStatus.OK);
            case 2:
                return new ResponseEntity<>("Phòng của hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
            case 3:
                return new ResponseEntity<>("Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
            case 4:
                return new ResponseEntity<>("Phòng đăng ký đã hết chỗ", HttpStatus.BAD_REQUEST);
            case 5:
                return new ResponseEntity<>("Hóa đơn không tồn tại", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Thanh toán thất bại, lỗi không xác định", HttpStatus.BAD_REQUEST);
    }

}