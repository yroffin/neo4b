var app = angular.module('BoardApp');

app.directive('backImg', function(){
    return function(scope, element, attrs){
        var url = attrs.backImg;
        element.css({
            'background-image': 'url(' + url +')',
            'height': '100%'
        });
    };
});
