
public class File(){
     @RequestMapping(value="/singleUpload")
    public String singleUpload(){
        return "singleUpload";
    }
    @RequestMapping(value="/singleSave", method=RequestMethod.POST )
    public @ResponseBody String singleSave(@RequestParam("file") MultipartFile file, @RequestParam("desc") String desc ){
        System.out.println("File Description:"+desc);
        String fileName = null;
        if (!file.isEmpty()) {
            try {
                fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                BufferedOutputStream buffStream = 
                        new BufferedOutputStream(new FileOutputStream(new File("F:/cp/" + fileName)));
                buffStream.write(bytes);
                buffStream.close();
                return "You have successfully uploaded " + fileName;
            } catch (Exception e) {
                return "You failed to upload " + fileName + ": " + e.getMessage();
            }
        } else {
            return "Unable to upload. File is empty.";
        }
    }
    @RequestMapping(value="/multipleUpload")
    public String multiUpload(){
        return "multipleUpload";
    }
    @RequestMapping(value="/multipleSave", method=RequestMethod.POST )
    public @ResponseBody String multipleSave(@RequestParam("file") MultipartFile[] files){
        String fileName = null;
        String msg = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream = 
                            new BufferedOutputStream(new FileOutputStream(new File("F:/cp/" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +"<br/>";
                } catch (Exception e) {
                    return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            return msg;
        } else {
            return "Unable to upload. File is empty.";
        }
    }


    @PostMapping("/post-template-goal")
    public ResponseEntity<?> downloadTemplate(@RequestBody Map<String,Object> input) throws Exception {
    log.create(input);
    byte[] fileData = egs.downloadTemplateByFilter(input);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", input.get("jobPosition").toString()+"_"+input.get("year").toString()+".xlsx"); // Set the desired filename

         String desc = map.get("description").toString();
        String year = map.get("year").toString();
        String job = map.get("jobPosition").toString();
        byte[] file = null;

        desc = "%"+desc.replace(",", "%")+"%";

        List<Object[]> obj = ulr.getScaleComponentByFilter(desc, year, job);
        Object[] o = obj.get(0);
        file = (byte[])o[2];
        return file;
        return new ResponseEntity<>(fileData, headers, org.springframework.http.HttpStatus.OK);
    }
}