/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.servlets;

import com.app.daos.OrderDAO;
import com.app.daos.OrderDetailsDAO;
import com.app.daos.OrderStatusDAO;
import com.app.daos.PaymentMethodDAO;
import com.app.daos.UserDAO;
import com.app.dtos.OrderDTO;
import com.app.dtos.OrderDetailsDTO;
import com.app.dtos.OrderStatusDTO;
import com.app.dtos.PaymentMethodDTO;
import com.app.dtos.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author DuyNK
 */
public class OrderDetailsServlet extends HttpServlet {
    private final String SUCCESS = "order_details.jsp";
    private final String FAIL = "error.html";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = FAIL;
        
        try {
            HttpSession session = request.getSession();
           
            if(session.getAttribute("ORDER_STATUS_LIST") == null){
                OrderStatusDAO dao = new OrderStatusDAO();
                List<OrderStatusDTO> statusList = dao.getAllOrderStatus();
                session.setAttribute("ORDER_STATUS_LIST", statusList);
            }
            int orderID = Integer.parseInt(request.getParameter("id"));
            
            OrderDAO orderDAO = new OrderDAO();
            OrderDTO order = orderDAO.getOrderByID(orderID);
            if(order != null){
                String userID = order.getUserID();
                String paymentMethodID = order.getPaymentMethodID();
                // get customer info
                UserDAO userDAO = new UserDAO();
                UserDTO customer = userDAO.getUserByID(userID);
                // get payment method info
                PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
                PaymentMethodDTO paymentMethod = paymentMethodDAO.getPaymentMethodByID(paymentMethodID);
                               
                // get all order details of the order
                OrderDetailsDAO detailDAO = new OrderDetailsDAO();
                List<OrderDetailsDTO> details = detailDAO.getAllOrderDetailsByOrderID(orderID);
                
                url = SUCCESS;
                
                request.setAttribute("ORDER", order);              
                request.setAttribute("CUSTOMER", customer);
                request.setAttribute("METHOD", paymentMethod);
                request.setAttribute("ORDER_DETAILS", details);
                
            }
        } catch (Exception e) {
        }
        finally{
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
