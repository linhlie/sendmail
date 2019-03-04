fileAppControllers.controller('FileCtrl', ['scope',
    function ($scope) {

        $scope.partialDownloadLink = 'http://localhost:8088/download?filename=';
        $scope.filename = '';

        $scope.uploadFile = function() {
            $scope.processQueue();
        };

        $scope.reset = function() {
            $scope.resetDropzone();
        };
    }

]);