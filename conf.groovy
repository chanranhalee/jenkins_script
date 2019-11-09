build_name = 'npu'

build_config = [
    'builds': [
        ['build.firmware'],
        ['build.caffe', 'build.npuc', 'build.tflitegen', 'generate.tflite'],
        ['build.framework_dd']
    ]
    'tests': [
        ['test.eden'],
        ['test.benchmark'],
        ['test.unittest']
    ]
]
