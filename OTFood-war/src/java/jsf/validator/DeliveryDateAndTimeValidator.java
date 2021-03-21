/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validator;

/**
 *
 * @author Ooi Jun Hao
 */
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;



@FacesValidator(value = "deliveryDateAndTimeValidator")
public class DeliveryDateAndTimeValidator implements Validator
{
    public DeliveryDateAndTimeValidator()
    {
    }
    
    
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        Date curr = new Date();
        
        try
        {
            if(value != null && value instanceof Date)
            {
                Date given = (Date)value;
                if(given.after(curr))
                {
                    return;
                }
                else {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deliver date and time should be in the future", null));
                }
            }
            else
            {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deliver date and time is required", null));
            }
        }
        catch(NumberFormatException ex)
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Option must be a valid date and time", null));
        }
        
    }
}