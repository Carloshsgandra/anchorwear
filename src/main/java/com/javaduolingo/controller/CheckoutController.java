package com.javaduolingo.controller;

import com.javaduolingo.dto.CheckoutForm;
import com.javaduolingo.model.Address;
import com.javaduolingo.model.Order;
import com.javaduolingo.model.User;
import com.javaduolingo.repository.AddressRepository;
import com.javaduolingo.service.OrderService;
import com.javaduolingo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final UserService userService;
    private final OrderService orderService;
    private final AddressRepository addressRepository;

    public CheckoutController(UserService userService, OrderService orderService,
                               AddressRepository addressRepository) {
        this.userService = userService;
        this.orderService = orderService;
        this.addressRepository = addressRepository;
    }

    private User getUser(UserDetails ud) {
        return userService.findByEmail(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @GetMapping
    public String checkoutPage(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        CheckoutForm form = new CheckoutForm();
        form.setShippingName(user.getName());

        // Pré-preencher com endereço padrão, se existir
        Optional<Address> defaultAddr = addressRepository.findByUserAndDefaultAddressTrue(user);
        defaultAddr.ifPresent(a -> {
            form.setShippingStreet(a.getStreet());
            form.setShippingNumber(a.getNumber());
            form.setShippingComplement(a.getComplement());
            form.setShippingNeighborhood(a.getNeighborhood());
            form.setShippingCity(a.getCity());
            form.setShippingState(a.getState());
            form.setShippingZip(a.getZip());
        });

        model.addAttribute("user", user);
        model.addAttribute("form", form);
        model.addAttribute("savedAddresses", addressRepository.findByUser(user));
        model.addAttribute("page", "checkout");
        return "checkout/index";
    }

    @PostMapping
    public String placeOrder(@AuthenticationPrincipal UserDetails ud,
                             @Valid @ModelAttribute("form") CheckoutForm form,
                             BindingResult result, Model model,
                             RedirectAttributes redirectAttributes) {
        User user = getUser(ud);

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("savedAddresses", addressRepository.findByUser(user));
            model.addAttribute("page", "checkout");
            return "checkout/index";
        }

        try {
            Order order = orderService.createOrder(user, form);

            // Salvar endereço se solicitado
            if (form.isSaveAddress()) {
                Address addr = new Address();
                addr.setUser(user);
                addr.setLabel("Casa");
                addr.setStreet(form.getShippingStreet());
                addr.setNumber(form.getShippingNumber());
                addr.setComplement(form.getShippingComplement());
                addr.setNeighborhood(form.getShippingNeighborhood());
                addr.setCity(form.getShippingCity());
                addr.setState(form.getShippingState());
                addr.setZip(form.getShippingZip());
                addr.setDefaultAddress(addressRepository.findByUser(user).isEmpty());
                addressRepository.save(addr);
            }

            return "redirect:/checkout/sucesso/" + order.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("savedAddresses", addressRepository.findByUser(user));
            model.addAttribute("page", "checkout");
            return "checkout/index";
        }
    }

    @GetMapping("/sucesso/{orderId}")
    public String success(@PathVariable Long orderId,
                          @AuthenticationPrincipal UserDetails ud, Model model) {
        User user = getUser(ud);
        Optional<Order> orderOpt = orderService.findById(orderId);
        if (orderOpt.isEmpty() || !orderOpt.get().getUser().getId().equals(user.getId())) {
            return "redirect:/conta/pedidos";
        }
        model.addAttribute("user", user);
        model.addAttribute("order", orderOpt.get());
        model.addAttribute("page", "checkout");
        return "checkout/sucesso";
    }
}
