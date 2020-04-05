import React from 'react';
import {post} from 'axios';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      file: null,
      prefixSize: 3,
      outputSize: 500,
      output: '',
      errorText:''
    };

    this.onFormSubmit = this.onFormSubmit.bind(this)
    this.onChange = this.onChange.bind(this)
    this.onChangePrefix = this.onChangePrefix.bind(this)
    this.onChangeOutput = this.onChangeOutput.bind(this)
    this.fileUpload = this.fileUpload.bind(this)
  }

  resetState() {
    this.setState({file: null,
        prefixSize: 3,
        outputSize: 500});
  }

  onFormSubmit(e) {
    e.preventDefault()
    if(this.state.file === null) {
      this.setState({errorText:'No File chosen', output: ''});
      return;
    }
    this.fileUpload(this.state.file, this.state.prefixSize,
        this.state.outputSize)
    .then((response) => {
      console.log(response.data);
      this.setState({output: 'Output ::: ' + response.data, errorText: ''})
      this.resetState();
    })
    .catch((error) => {
      this.setState({errorText: "File could not be processed", output:''})
      this.resetState();
    })
  }

  onChange(e) {
    this.setState({file: e.target.files[0], errorText:''})
  }

  onChangePrefix(e) {
    this.setState({prefixSize: e.target.value})
  }

  onChangeOutput(e) {
    this.setState({outputSize: e.target.value})
  }

  fileUpload(file, prefixSize, outputSize) {
    const url = 'http://localhost:9001/v1/transform';
    const formData = new FormData();
    formData.append('file', file)
    formData.append('prefixSize', prefixSize);
    formData.append('outputSize', outputSize);
    const config = {
      headers: {
        'content-type': 'multipart/form-data'
      }
    }
    return post(url, formData, config)
  }

  render() {
    return (
        <form onSubmit={this.onFormSubmit}>
          <Container maxWidth="lg">

              <Typography variant="h5" component="h1" gutterBottom>
                Select .txt file to upload
              </Typography>
              <div>
                <Button variant="contained" component="label" color="secondary">
                  Upload File
                  <input type="file" accept=".txt" onChange={this.onChange}/>
                </Button>
              </div>
              <div>
                <TextField label="Prefix Count(words)" defaultValue={3}
                           onChange={this.onChangePrefix}/>
              </div>
              <div>
                <TextField label="OutputSize(chars), Ignore=0" defaultValue={500}
                           onChange={this.onChangeOutput}/>
              </div>
              <div>
                <Box flexDirection="row" m="2">
                <Button type={"submit"} variant="contained"
                        color="primary">Submit</Button>
                </Box>
              </div>
              <div>
                <Container>{this.state.errorText}</Container>
              </div>
              <div>
                <Container>{this.state.output}</Container>
              </div>

          </Container>
        </form>
    );
  }
}

export default App
