
(function () {
    "use strict";
    var attachmentDropzone;
    var attachmentDropzoneId ="#attachment-dropzone-tab";
    $(function () {
        initDropzone();
    })

    function initDropzone() {
        Dropzone.autoDiscover = false;
        attachmentDropzone = new Dropzone("div" + attachmentDropzoneId, {
            url: "/",
            addRemoveLinks: true,
            maxFilesize: 2,
            filesizeBase: 1000,
            dictRemoveFile: "削除",
            dictCancelUpload: "キャンセル",
            dictDefaultMessage: "Drop files here or click to upload.",
            init: function() {
                this.on("success", function(file, response) {
                    if(response && response.status){
                        var data = response.list && response.list.length > 0 ? response.list[0] : null;
                        if(data){
                            file.id = data.id;
                        }
                    }
                });
                this.on("removedfile", function(file) {
                    if(!!file && !!file.upload && !!file.id){
                        removeFile(file.id)
                    }
                });
            },
            thumbnail: function(file, dataUrl) {}
        })
    }
})(jQuery);
