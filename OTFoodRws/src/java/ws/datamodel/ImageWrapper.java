/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

/**
 *
 * @author Ooi Jun Hao
 */
public class ImageWrapper {
    private String base64Code;
    
    public ImageWrapper(String base64Code){
        this.base64Code = base64Code;
    }

    public String getBase64Code() {
        return base64Code;
    }

    public void setBase64Code(String base64Code) {
        this.base64Code = base64Code;
    } 
    
}
